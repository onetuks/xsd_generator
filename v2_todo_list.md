# XSD Generator v2 개선 TODO List

> 분석 대상: `src/main/java` 전체 (Main, core, model, definition, specification, hierarchy, util, ui)
> 분석 기준일: 2026-09-08
> 우선순위: 🔴 P0(꼭 고쳐야 함, 데이터 손상/오작동) · 🟡 P1(개선 권장) · 🟢 P2(있으면 좋음)

---

## 1. 취약점 (Security / Robustness)

- [x] 🔴 **생성되는 XSD 문자열에 XML 이스케이프 처리가 전혀 없음**
  `core/XsdGenerator.java` 전체 (`String.format`으로 name/description/namespace를 그대로 삽입)
  Description이나 필드명에 `<`, `>`, `&`, `"` 등이 포함되면 XSD 자체가 깨지거나 의도치 않은 태그가 주입될 수 있음. `escapeXml()` 유틸을 만들어 `DataTypeEntity`의 name/description/namespace를 태그에 삽입하기 전에 반드시 이스케이프 처리해야 함.

- [x] 🟡 **저장 경로/파일명에 대한 검증 없음 (경로 조작 가능)**
  `util/FileSaver.java:15-19`, `definition/components/DataTypeDefinitionInfoComponent.java` (Target Dir, DT Name이 모두 자유 텍스트 입력)
  Target Dir과 DT/MT Name이 자유 텍스트라서 `..\..\` 같은 상대경로나 금지 문자를 입력하면 의도하지 않은 위치에 파일이 생성될 수 있음. Target Dir은 `JFileChooser`로 고정하고(직접 타이핑 비활성화), DT/MT Name은 파일명으로 쓸 수 없는 문자를 검증하도록 개선 필요.
  → Target Dir 텍스트필드를 `setEditable(false)`로 고정해 반드시 `JFileChooser`를 거치도록 하고, `DataTypeMeta.validate()`에서 DT/MT Name에 `..`나 `\ / : * ? " < > |` 포함 시 예외를 던지도록 추가.

- [x] 🟡 **기존 파일 덮어쓰기 시 경고 없음**
  `util/FileSaver.java:20-25`
  동일 이름의 `.xsd` 파일이 이미 존재해도 아무 확인 없이 덮어씀. 저장 전 파일 존재 여부를 확인해 확인 다이얼로그를 띄우는 로직 추가 필요.
  → `FileSaver.exists()` + `DataTypePipelineService.willOverwriteExistingFile()` 추가, Hierarchy의 "Complete" 버튼에서 덮어쓰기 전 확인 다이얼로그 표시. 부가적으로 `FileSaver`가 플랫폼 기본 인코딩(MS949) 대신 항상 UTF-8로 파일을 쓰도록 수정(XSD 선언부의 `encoding="UTF-8"`과 실제 바이트가 불일치해 한글 설명이 깨지던 문제도 함께 해결).

- [x] 🟢 **아이콘 리소스 로딩 실패 시 전체 앱 크래시**
  `util/IconLoader.java:18` (`Objects.requireNonNull(getClass().getResource(...))`)
  리소스가 누락되면 NPE로 앱 전체가 죽음. 배포 패키징 실수 시 사용자에게 알아보기 힘든 크래시로 이어지므로, 최소한 사용자 친화적 에러 처리(기본 아이콘 대체 등)로 완화 필요.
  → 리소스를 찾지 못하거나 읽기 실패 시 예외 대신 빈 아이콘으로 대체하고 콘솔에 원인을 남기도록 수정.

---

## 2. 불완전 구현 기능

- [x] 🔴 **`Type.NUMBER`가 잘못된 XSD 내장 타입을 참조함**
  `model/vo/Type.java:5` (`NUMBER("xsd:number")`)
  `xsd:number`는 XML Schema 표준 내장 타입이 아님(올바른 타입은 `xsd:decimal`, `xsd:integer`, `xsd:double` 등). 이 타입으로 생성된 필드는 SAP PO에서 스키마 검증 실패로 이어질 가능성이 높음. 실제 사용 가능한 숫자 타입으로 교체 필요.

- [x] 🟡 **"Jdbc Manipulation" 패널이 실제 JDBC 연동 없이 정적 템플릿만 삽입**
  `definition/components/DataTypeDefinitionJdbcStructurePanel.java`, `definition/services/JdbcStructureInvocator.java`
  이름과 달리 실제 DB에 연결해 테이블/컬럼을 읽어오지 않고, 고정된 필드명 템플릿 문자열만 텍스트영역에 추가함. 실제 JDBC 인트로스펙션 기능으로 완성하거나, 오해를 줄이도록 "Template Insert" 등으로 명칭을 바꾸는 결정이 필요함.
  → 실제 DB 연동(드라이버/커넥션/자격증명 UI 등)은 별도 요구사항 정의가 필요한 큰 작업이라 이번 범위에서는 제외하고, 오해를 줄이는 쪽으로 결정: 제목을 "SQL Template Insert"로 바꾸고 각 버튼에 "실제 DB에 연결하지 않는다"는 툴팁을 추가.

- [x] 🔴 **테스트 코드가 전혀 없음**
  `src/test` 디렉토리 자체가 존재하지 않음
  트리 탐색/재배치(`DataTypePipelineService`), XSD 문자열 생성(`XsdGenerator`), 필드 파싱(`DataTypeFieldParser`) 등 핵심 로직에 대한 단위 테스트가 전무함. 리팩토링/기능 추가 시 회귀를 잡을 안전망이 없으므로 우선적으로 핵심 서비스 클래스부터 테스트 추가 필요.
  → `XsdGeneratorTest`, `DataTypePipelineServiceTest`, `DataTypeFieldParserTest` 추가(13개 케이스, JUnit 5). **참고**: 이 개발 환경(Windows + 경로에 한글 포함)에서는 `gradle test` 실행 시 Gradle 테스트 워커가 클래스패스를 찾지 못하는 환경적 제약(Gradle의 알려진 비-ASCII 경로 이슈로 추정)이 있어 CLI에서 직접 실행이 막힘. 대신 JUnit Platform Launcher를 수동 구성해 13개 테스트 전부 통과를 확인함. IntelliJ에서 "Run tests using: IntelliJ IDEA"로 설정하면 정상 동작할 가능성이 높음.

- [x] 🟡 **Category=ELEMENT에서도 Occurrence "optional" 선택이 가능해 잘못된 XSD 생성 위험**
  `model/vo/Occurrence.java:37-39,73-79`, `specification/elements/DataTypeElementSpecificationComboBoxFactory.java:64-73`, `core/XsdGenerator.java:93-99`
  "optional"은 원래 ATTRIBUTE 전용으로 설계된 값(`upperBound=null`)인데, Occurrence 콤보박스는 카테고리 구분 없이 항상 전체 옵션을 보여줌. ELEMENT 카테고리 필드에 "optional"을 선택하면 `getUpperBound()`가 null을 반환해 생성된 XSD에 `maxOccurs="null"`이 그대로 찍히는 오류가 발생함. 카테고리에 따라 콤보박스 옵션을 분리하거나, ELEMENT일 때 "optional" 선택을 막아야 함.
  → `Occurrence.getOccurrenceCombo(Category)`로 옵션을 분리(ATTRIBUTE는 "optional"만, 그 외는 bound 쌍 4종만)하고, ATTRIBUTE일 때는 콤보박스 자체를 비활성화. Category 변경 시 해당 행이 다시 그려지도록 `DataTypeSpecificationPanel.refreshElements()` 추가. `OccurrenceTest` 추가.

- [x] 🟢 **README에 명시된 "action 속성은 다른 attribute를 가질 수 없도록 강제"가 실제로 UI에서 강제되지 않음**
  `specification/elements/DataTypeElementSpecificationCheckBoxFactory.java:22-23`, `core/XsdGenerator.java:82-87`
  실제로는 체크박스 목록에서 ACTION만 제외될 뿐, "action"이라는 이름의 필드를 Category=ATTRIBUTE로 수동 지정하는 것 자체를 막는 로직은 없음. 관련 로직은 XSD 생성 시 태그 형태(extension vs wrapper)를 결정하는 데만 쓰임. 문서와 실제 동작을 일치시키거나(강제 로직 추가), 문서 표현을 "속성 편집 제한"이 아닌 "생성 규칙"으로 수정 필요.
  → README 문구를 실제 동작(체크박스 제외 + 생성 시 태그 구조 분기)에 맞게 수정하고, `XsdGenerator.appendElementTag`의 분기 로직에 왜 그렇게 나뉘는지 설명하는 주석 추가.

- [ ] 🟢 **프로젝트 저장/불러오기 기능 부재**
  전역 (상태는 `DataTypeState` 인메모리에만 존재, 파일 직렬화 없음)
  작업 중 앱이 종료되면 Definition/Specification/Hierarchy 입력 전체가 소실됨. JSON 등으로 현재 작업 상태를 저장/불러오는 기능이 없어 대규모 DT 작업 시 리스크가 큼.

---

## 3. 기존 기능의 코드 냄새 / 불편함

- [x] 🟡 **참조 동일성 기반의 불필요한 "재탐색" 패턴 반복**
  `specification/elements/DataTypeElementSpecificationCheckBoxFactory.java:40-43`, `DataTypeElementSpecificationComboBoxFactory.java:31-35`, `DataTypeElementSpecificationTextFieldFactory.java:51-54`
  이미 리스트에서 꺼내 전달받은 `element` 객체를 다시 `getDataTypeElements().stream().filter(e -> e == element).findAny().orElseThrow(...)`로 재검색함. 무의미한 O(n) 탐색과 `RuntimeException` 위험만 추가하는 코드로, 그냥 전달받은 `element`를 직접 수정하면 됨.
  → 세 팩토리 모두 전달받은 `element`를 직접 수정하도록 정리. `specification` 참조가 더 이상 필요 없어진 CheckBox/TextField 팩토리는 생성자 시그니처 일관성만 유지하고 필드 저장은 제거.

- [x] 🟡 **`model.DataTypeNode`가 `specification.elements.DataTypeElement`에 의존하는 레이어 위반**
  `model/DataTypeNode.java:11,33-34`
  모델 계층이 UI/스펙 계층의 상수(`DataTypeElement.STATEMENT`)를 문자열 매칭으로 참조함. 계층 의존 방향이 역전되어 있어 향후 리팩토링/모듈 분리를 어렵게 만듦. `STATEMENT` 같은 도메인 상수는 `model` 쪽으로 옮기거나 별도 공용 상수 클래스로 분리 필요.
  → `model.vo.FieldName`에 `STATEMENT` 상수를 두고 `DataTypeNode`가 이를 참조하도록 변경. `DataTypeElement.STATEMENT`는 기존 공개 API를 유지하면서 같은 상수를 위임 참조.

- [x] 🟡 **XSD 문자열을 순수 문자열 concat/format으로 생성**
  `core/XsdGenerator.java` 전체
  이스케이프 누락(1번 항목)뿐 아니라, 들여쓰기 없는 한 줄짜리 XSD가 생성되어 Git diff/사람이 읽기 어려움. `XMLStreamWriter` 또는 DOM+`Transformer`(`INDENT`) 기반으로 전환하면 이스케이프와 포매팅 문제를 동시에 해결 가능.
  → 이스케이프는 이미 해결된 상태였고, `XMLStreamWriter`/DOM으로의 전체 전환은 기존 생성 로직에 녹아있는 스키마 분기 규칙(extension vs wrapper 등)을 다시 짜야 해서 위험 대비 이득이 낮다고 판단. 대신 기존 문자열 조립 방식은 유지한 채 depth 기반 들여쓰기(`indent(depth)`)만 추가해 Git diff/가독성 문제를 해결. 실제 생성 결과를 수동으로 출력해 들여쓰기·이스케이프가 의도대로 나오는지 확인함.

- [x] 🟢 **`FileSaver`의 경로 결합 로직이 수동 문자열 처리 + 구분자 불일치**
  `util/FileSaver.java:15-18`
  `charAt(len-1) == '/'`만 검사하고 Windows 기본 구분자인 `\`는 고려하지 않아 `D:\` 같은 기본값에서 `D:\/파일명.xsd` 형태의 혼용 경로가 만들어짐. `java.nio.file.Path.resolve()`로 교체 권장.
  → `Paths.get(dirPath, filename + EXTENSION)`으로 교체(덮어쓰기 경고 항목과 함께 처리).

- [ ] 🟢 **`DataTypeNode`에 부모 참조가 없어 트리 조작마다 BFS 전체 탐색**
  `core/DataTypePipelineService.java:94-128` (`findNode`, `findParentNode`)
  Hierarchy 화면에서 노드 이동/포커스할 때마다 루트부터 큐 탐색을 수행함. `DataTypeNode`에 부모 참조 필드를 추가하면 대부분의 탐색 로직을 O(1)로 단순화 가능.

- [x] 🟢 **하드코딩된 기본 저장 경로 `"D:\\"`**
  `definition/components/DataTypeDefinitionInfoComponent.java:81`
  D 드라이브가 없는 환경(노트북 등)에서 기본값이 무의미함. 사용자 홈 디렉터리나 빈 값으로 대체 필요.
  → `System.getProperty("user.home")`으로 대체.

- [x] 🟢 **문자열 비교(`label.contains(...)`)로 컴포넌트 역할을 분기하는 `DataTypeDefinitionInfoComponent`**
  `definition/components/DataTypeDefinitionInfoComponent.java:41-84`
  라벨 문자열에 따라 디렉터리 선택 버튼/MT 체크박스 유무를 결정하는 방식이라, 새로운 필드 타입 추가 시 문자열 매칭 조건이 계속 늘어나는 구조. enum 기반 필드 타입 정의로 리팩토링 권장.
  → `InfoFieldType` enum(라벨 포함)으로 교체하고 문자열 비교 분기를 enum 비교로 정리.

- [x] 🟢 **Hierarchy 우클릭 시 사용자가 Manipulation Type 선택을 취소하면 `IllegalArgumentException`이 미처리 상태로 전파**
  `hierarchy/components/DataTypeHierarchyScrollPane.java:97-106`
  다이얼로그를 취소해도 예외가 던져지고 어디서도 catch되지 않아 스택트레이스만 콘솔에 출력됨(사용자는 아무 피드백도 못 받음). 취소는 예외 상황이 아니라 정상 흐름이므로 단순 `return`으로 처리 필요.
  → 취소 시 예외 대신 `null`을 반환하도록 바꾸고, 호출부에서 `null`이면 조용히 무시하고 리턴하도록 수정.

---

## 4. 전체 프로세스의 불편함

- [x] 🔴 **Specification → Hierarchy 이동 시 기존 Hierarchy 수동 작업이 경고 없이 전부 초기화됨**
  `specification/components/DataTypeSpecificationButtonPanel.java:31-39` (`updateDataTypeNode` 매번 재호출), `hierarchy/DataTypeHierarchyPanel.java:50-57`
  Hierarchy에서 공들여 재배치한 구조가 있어도, Specification 화면으로 "Prev"했다가 다시 "Next"만 눌러도 트리가 자동배치 규칙으로 완전히 재생성됨. Hierarchy의 "Reset" 버튼에는 확인 다이얼로그가 있지만 이 경로에는 전혀 없음 — 동일한 수준의 경고가 필요함.

- [x] 🟡 **최종 XSD 파일을 디스크에 쓰기 전 미리보기가 없음**
  `hierarchy/DataTypeHierarchyPanel.java:96-99`
  "Complete" 버튼을 누르면 바로 파일이 생성됨. 생성될 XSD 내용을 미리 검토(또는 복사)할 수 있는 미리보기 창이 없어 결과를 확인하려면 파일을 직접 열어야 함.
  → `DataTypePipelineService`에 저장하지 않고 문자열만 반환하는 `previewDT()`/`previewMT()` 추가(`generateXSDFile()`도 이를 재사용하도록 정리). "Complete" 클릭 시 스크롤 가능한 읽기 전용 미리보기(모노스페이스 폰트) + 저장/취소 확인 다이얼로그를 띄우며, 기존 덮어쓰기 경고 문구도 이 다이얼로그로 통합.

- [x] 🟡 **Definition 단계에서 필드를 하나도 입력하지 않아도 다음 단계로 진행 가능**
  `definition/components/DataTypeDefinitionButtonPanel.java:34-52`
  메타 정보(DT명/Namespace/경로)만 검증하고 필드 목록이 비어있는지는 검증하지 않음. 빈 Specification 화면으로 넘어가 사용자가 혼란을 겪을 수 있음.
  → 파싱된 필드 목록이 비어있으면 "필드를 하나 이상 입력해주세요." 에러를 던지고 화면 전환을 막도록 추가.

- [x] 🟡 **Name/Description을 줄 번호로 매칭하는 입력 방식이 오류에 취약**
  `definition/services/DataTypeFieldParser.java:14-32`, `definition/components/DataTypeDefinitionFieldPanel.java`
  두 개의 별도 `JTextArea`(Name/Description)를 줄 인덱스로 매핑하는 구조라, 한쪽에 줄을 빠뜨리면 이후 모든 필드의 설명이 한 줄씩 밀려서 잘못 매칭됨. 이름-설명을 한 행으로 묶어 편집하는 테이블(JTable) 기반 입력기로 전환하면 이 문제 자체가 사라짐.
  → JTable로의 전면 전환은 보류. 이 화면의 핵심 사용 패턴이 엑셀 등에서 필드명 열/설명 열을 각각 복사해 두 텍스트영역에 통째로 붙여넣는 것으로 보이는데, JTable로 바꾸면 이 멀티라인 붙여넣기 워크플로우가 깨질 위험이 커서(별도의 커스텀 붙여넣기 핸들러 필요) 시각적 검증이 불가능한 상태로 다루기엔 위험 대비 이득이 낮다고 판단. 대신 "Next" 클릭 시 각 탭의 Name/Description 줄 수가 다르면 구체적인 줄 수 차이를 보여주는 경고 다이얼로그를 띄워 사용자가 밀림을 인지하고 계속할지 선택하게 함.

- [x] 🟢 **Structure Manipulation의 "Remove"가 항상 마지막 탭만 제거 가능**
  `definition/components/DataTypeDefinitionStructureManipulationPanel.java:38-40`
  중간 탭을 지우려면 그 뒤의 모든 탭을 순서대로 지웠다가 다시 만들어야 함. 각 탭에 개별 닫기 버튼을 추가하는 것이 자연스러움.
  → 각 탭에 개별 닫기(x) 버튼을 붙이고 닫을 때마다 탭 번호를 재정렬. 마지막 남은 탭은 닫을 수 없도록 가드. 커스텀 탭 헤더 컴포넌트를 붙이면서 `JTabbedPane.getComponents()`가 헤더까지 포함하게 되는 부작용을 발견해, 필드 추출/줄 수 검증 로직도 `getComponentAt(index)` 기반으로 함께 수정.

- [ ] 🟢 **Jdbc Manipulation 버튼이 "현재 선택된 탭"에 삽입되는데 실수로 탭을 잘못 선택한 채 클릭하면 되돌릴 방법이 텍스트 직접 삭제뿐**
  `definition/components/DataTypeDefinitionJdbcStructurePanel.java:34-63`
  템플릿을 잘못된 탭에 삽입했을 때 이를 되돌리는 전용 기능(예: "마지막 삽입 취소")이 없음.

- [ ] 🟢 **Hierarchy 재배치 UX가 좌클릭(포커스)+우클릭(대상)+모달 선택창 조합으로 비직관적**
  `hierarchy/components/DataTypeHierarchyScrollPane.java:45-73`
  `JTree`는 네이티브 드래그앤드롭을 지원함에도 클릭 조합 + 팝업 다이얼로그 방식을 사용해 학습 곡선이 높음. 드래그앤드롭 기반 재배치로 전환하면 훨씬 직관적임.

- [ ] 🟢 **3단계 전체 흐름에 진행 상태 표시(스텝 인디케이터)가 없음**
  `Main.java`, 각 Panel 클래스
  Definition/Specification/Hierarchy 중 현재 어디에 있는지, 이전 단계가 유효한지 한눈에 보여주는 브레드크럼/스텝 바가 없어 큰 데이터를 다룰 때 방향감을 잃기 쉬움.

---

## 5. UX의 불편함

- [x] 🟡 **Definition 화면의 "Reset" 버튼에 확인 다이얼로그가 없음**
  `definition/components/DataTypeDefinitionButtonPanel.java:25-32`
  Hierarchy의 "Reset"과 달리 클릭 즉시 모든 입력(DT/MT명, Namespace, 모든 필드 탭)이 삭제됨. 동일한 확인 다이얼로그 패턴 적용 필요.
  → Hierarchy Reset과 동일한 패턴의 확인 다이얼로그 추가.

- [x] 🟡 **MT Name 체크박스에 설명 텍스트/툴팁이 없음**
  `definition/components/DataTypeDefinitionInfoComponent.java:50-64`
  라벨 없는 빈 체크박스만 덩그러니 있어 처음 쓰는 사용자는 이게 무슨 기능인지 알기 어려움. "MT 파일도 함께 생성" 같은 라벨/툴팁 추가 필요.
  → 체크박스에 "MT 파일도 함께 생성" 라벨과 툴팁 추가.

- [x] 🟢 **필드 중복 이름에 대한 경고가 없음**
  `specification/elements/DataTypeElementSpecificationTextFieldFactory.java`
  같은 이름의 필드를 여러 개 만들어도 아무 경고가 없어, 의도치 않게 동일 이름 element가 여러 개 생성될 수 있음.
  → Specification → Hierarchy로 넘어가는 "Next" 클릭 시 중복된 이름을 모아 보여주는 확인 다이얼로그 추가.

- [ ] 🟢 **에러 다이얼로그가 개발자용 메시지를 그대로 노출**
  `definition/components/DataTypeDefinitionButtonPanel.java:47-49`, `model/DataTypeMeta.java:22-28`
  `"Invalid data type name: " + dtName` 같은 원문 예외 메시지가 그대로 사용자에게 노출됨. SAP PO 담당자가 이해하기 쉬운 한국어 안내 문구로 다듬을 필요가 있음.

- [x] 🟢 **`hasQuot` / `isInput` / `isOutput` 속성 체크박스에 설명(툴팁)이 없음**
  `specification/elements/DataTypeElementSpecificationCheckBoxFactory.java:30-54`
  각 속성이 실제로 무엇을 의미하는지 UI 상에서 전혀 안내되지 않아 신규 사용자는 별도 문서 없이는 이해하기 어려움.
  → `Attribute` enum에 설명 필드를 추가하고 체크박스 툴팁으로 노출.

- [x] 🟢 **Hierarchy 우클릭 시 EditMode 꺼짐 경고가 "확인" 버튼 하나뿐인 `showConfirmDialog`로 실제로는 알림(Alert)에 가까움**
  `hierarchy/components/DataTypeHierarchyScrollPane.java:51-55`
  `showConfirmDialog`를 쓰면서 옵션은 사실상 "확인"뿐이라 질문처럼 보이지만 아무 선택도 할 수 없음. `showMessageDialog`로 바꾸는 것이 의미상 정확함.
  → `showMessageDialog`로 교체.

- [ ] 🟢 **Specification 화면에 필드 검색/필터 기능이 없음**
  `specification/components/DataTypeSpecificationScrollPane.java`
  필드가 수십~수백 개인 DT의 경우 원하는 필드를 찾으려면 스크롤만으로 탐색해야 함. 이름 기준 필터 입력창 추가 권장.

- [ ] 🟢 **"XSD File Generated!" 완료 다이얼로그에 결과 확인 액션이 없음**
  `hierarchy/DataTypeHierarchyPanel.java:96-99`
  생성 완료 후 "폴더 열기"나 "파일 열기" 같은 바로가기가 없어 사용자가 직접 탐색기를 열어 경로를 찾아가야 함.

---

## 우선순위 요약

| 우선순위 | 개수 | 비고 |
|---|---|---|
| 🔴 P0 | 4 | 데이터 손상/오작동 직결 (XML 이스케이프, 테스트 부재, Type.NUMBER 오류, Hierarchy 무경고 초기화) |
| 🟡 P1 | 12 | 기능 완성도/신뢰성 개선 |
| 🟢 P2 | 15 | 코드 품질/UX 다듬기 |
