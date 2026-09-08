package model.vo;

/**
 * 필드명 패턴 기반 규칙(트리 삽입 순서, 레벨 자동 배치 등)에서 공통으로 참조하는 명명 규약 상수.
 * model 계층과 specification 계층이 모두 이 값을 참조해야 하므로, UI 계층(specification)의
 * 상수를 model이 거꾸로 참조하지 않도록 여기에 둔다.
 */
public interface FieldName {

  String STATEMENT = "StatementName";
}
