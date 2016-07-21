/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ui;

import app.settings.FolderTracker;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Naga Srinivas @Canvass
 */
public class FileSelectorDialog extends JDialog {

    private DefaultTableModel tableModel;
    private JButton okBtn;
    private JButton cancelBtn;

    private void init() {
        Object[] columnNames = {"Upload", "File Name", "Path", "File Type"};
        Object[][] data = { //{"Buy", "IBM", new Integer(1000), new Double(80.50), false},
        //{"Sell", "MicroSoft", new Integer(2000), new Double(6.25), true},
        //{"Sell", "Apple", new Integer(3000), new Double(7.35), true},
        //{"Buy", "Nortel", new Integer(4000), new Double(20.00), false}
        };
        okBtn = new JButton("Upload");
        cancelBtn = new JButton("Cancel");
        tableModel = new DefaultTableModel(data, columnNames);
        //model.addRow(columnNames);
        JTable table = new JTable(tableModel) {

            private static final long serialVersionUID = 1L;

            /*
             * @Override public Class getColumnClass(int column) { return
             * getValueAt(0, column).getClass(); }
             */
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        };
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JPanel dataPanel = new JPanel(new GridLayout(1, 1));
        JPanel btnPanel = new JPanel();

        //table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        JScrollPane scrollPane = new JScrollPane(table);
        //setLayout(new FlowLayout());
        dataPanel.add(scrollPane);
        panel.add(dataPanel);
        panel.add(btnPanel);

        getContentPane().add(panel);
        btnPanel.add(cancelBtn);
        btnPanel.add(okBtn);
    }

    public void show(FolderTracker tracker) {
        setVisible(true);
        List<Vector> rows = tracker.getAsTableRows();
        for (Vector obj : rows) {
            tableModel.addRow(obj);
        }
    }

    public List<String> getSelectedFiles() {
        List<String> selectedFiles = new ArrayList<String>();
        Vector data = tableModel.getDataVector();
        for (int i = 0; i < data.size(); i++) {
            Vector row = (Vector) data.get(i);
            if ((Boolean) row.get(0)) {
                selectedFiles.add(row.get(2).toString());
            }
        }
        return selectedFiles;
    }

    public static void main(String[] args) throws IllegalAccessException {
        FileSelectorDialog dialog = new FileSelectorDialog();
        dialog.init();
        //String folderPath = "D:\\Projects\\Canvass-E-Comm-test\\web\\src\\main\\webapp\\res\\designers";
        String folderPath = "D:\\Projects\\Canvass-E-Comm-test\\web\\src\\main\\webapp\\res";
        FolderTracker tracker = FolderTracker.load(folderPath);
        System.out.println(tracker);
        dialog.show(tracker);
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(null);
        System.out.println(dialog.getSelectedFiles());
    }
}
