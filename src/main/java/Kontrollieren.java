import javakarol.Roboter;
import javakarol.Welt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Kontrollieren  extends JFrame implements MouseListener {
    private final Welt welt;
    private final Roboter roboter;
    private final Box box;
    private final JButton befehl;
    private final JButton multi_befehl;
    public Kontrollieren() {
        welt = new Welt(10, 10, 10);
        roboter = new Roboter(1, 1, 'S', welt);
        box = new Box(BoxLayout.PAGE_AXIS);
        box.setAlignmentY(Component.CENTER_ALIGNMENT);
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        AlleTastenHinzufuegen(box);
        befehl = new JButton("Befehl Ausführen");
        befehl.setAlignmentX(CENTER_ALIGNMENT);
        befehl.addMouseListener(this);
        box.add(befehl);
        multi_befehl = new JButton("Mehre Befehle Ausführen");
        multi_befehl.setAlignmentX(CENTER_ALIGNMENT);
        multi_befehl.addMouseListener(this);
        box.add(multi_befehl);
        JLabel jLabel = new JLabel("Bitte wähle dieses Fenster aus, um Karol zu steuern");
        jLabel.setAlignmentX(CENTER_ALIGNMENT);
        box.add(jLabel);
        add(box);
        setTitle("Kontrollieren");
        pack();
        setVisible(true);
    }
    private void AlleTastenHinzufuegen(JComponent label) {
        TastenHinzufuegen("W", actionEvent -> roboter.Schritt(), label);
        TastenHinzufuegen("A", actionEvent -> roboter.LinksDrehen(), label);
        TastenHinzufuegen("D", actionEvent -> roboter.RechtsDrehen(), label);
    }
    private void TastenHinzufuegen(String keystroke, Consumer<ActionEvent> action, JComponent component) {
        String actionKey = keystroke + "_action";
        component.getInputMap().put(KeyStroke.getKeyStroke(keystroke), actionKey);
        component.getActionMap().put(actionKey, new AbstractAction(actionKey) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action.accept(actionEvent);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Bitte wähle den Befehl aus: "));
        List<String> optionen = new ArrayList<>();
        for (Aktionen value : Aktionen.values()) optionen.add(value.name());
        JComboBox comboBox = new JComboBox();
        optionen.forEach(comboBox::addItem);
        panel.add(comboBox);
        JTextField field = new JTextField();
        boolean isMulti = mouseEvent.getSource().equals(multi_befehl);
        if (isMulti) {
            panel.add(new JLabel("Anzahl:"));
            field = new JTextField("1");
            panel.add(field);
        }
        int result = JOptionPane.showConfirmDialog(null, panel, "Flavor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if (isMulti) {
                for (int i = 0; i < Integer.parseInt(field.getText()); i++) {
                    Aktionen.valueOf((String) comboBox.getSelectedItem()).AktionAusfuehren(roboter);
                }
                return;
            }
            Aktionen.valueOf((String) comboBox.getSelectedItem()).AktionAusfuehren(roboter);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {AlleTastenHinzufuegen(box);}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {AlleTastenHinzufuegen(box);}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    enum Aktionen {
        Schritt(Roboter::Schritt),
        LinksDrehen(Roboter::LinksDrehen),
        RechtsDrehen(Roboter::RechtsDrehen),
        Hinlegen(Roboter::Hinlegen),
        Aufheben(Roboter::Aufheben),
        QuaderAufstellen(Roboter::QuaderAufstellen);

        private final Consumer<Roboter> aktion;
        Aktionen(Consumer<Roboter> Aktion) {this.aktion = Aktion;}
        public void AktionAusfuehren(Roboter roboter) {aktion.accept(roboter);}
    }
}
