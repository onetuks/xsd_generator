package specification.components;

import specification.DataTypeSpecificationPanel;
import util.Navigator;

import javax.swing.*;
import java.awt.*;

public class DataTypeSpecificationButtonPanel extends JPanel {

    private final DataTypeSpecificationPanel specification;

    public DataTypeSpecificationButtonPanel(DataTypeSpecificationPanel specification) {
        super(new FlowLayout(FlowLayout.RIGHT));
        this.specification = specification;

        initComponent();
    }

    private void initComponent() {
        add(createPrevButton());
        add(createNextButton());
    }

//    private JButton createGenerateButton() {
//        JButton generateButton = new JButton("Generate");
//
//        generateButton.setBackground(FrameInfo.SECONDARY);
//        generateButton.setForeground(FrameInfo.TERTIARY);
//
//        generateButton.addActionListener(e -> {
//            fileSaver.saveFile(
//                    builder.getTree().getFileFullPath(),
//                    xsdGenerator.generate(builder.getTree()));
//
//            JOptionPane.showMessageDialog(this, "XSD File Generated!");
//        });
//
//        add(generateButton);
//    }

    private JButton createPrevButton() {
        JButton prevBtn = new JButton("Prev");
        prevBtn.addActionListener(e -> specification.getNavigator().showScreen(Navigator.DEFINITION));
        return prevBtn;
    }

    private JButton createNextButton() {
        JButton nextBtn = new JButton("Next");
        nextBtn.addActionListener(e -> {
            specification.getService().updateDataTypeNode(specification.getService().getDataTypeElements());
            specification.getNavigator().showScreen(Navigator.HIERARCHY);
        });
        return nextBtn;
    }
}
