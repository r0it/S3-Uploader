/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ui;

import app.aws.BaseAmazonS3Client;
import app.aws.S3StaticFilesUploader;
import app.events.FileEvent;
import app.settings.FolderTracker;
import app.settings.RecentValues;
import app.settings.UserSettings;
import com.amazonaws.services.s3.model.Bucket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Naga Srinivas @Canvass
 */
public class Main extends javax.swing.JFrame
        implements FileEvent {

    UserSettings settings;

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        setLocationRelativeTo(null);
        initalize();
    }

    public void doUpload() {
    }

    private void initalize() {
        final Main that = this;

        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileSelector = new FileSelector(this, true);
        jButton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedVal = fileChooser.showOpenDialog(null);
                if (selectedVal == JFileChooser.APPROVE_OPTION) {
                    sourcePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    settings.getRecentValues().setLocalFolder(sourcePath.getText());
                    UserSettings.save(settings); //saving locally
                    try {
                        tracker = FolderTracker.getInstance(sourcePath.getText());
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (tracker != null) {
                        fileSelector.show(tracker);
                    }
                }
            }
        });

        jButton1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = "";
                try {
                    RecentValues recent = settings.getRecentValues();
                    if (recent == null) {
                        recent = new RecentValues();
                    }
                    recent.setLocalFolder(sourcePath.getText());
                    recent.setS3Bucket(bucketList.getSelectedItem().toString());
                    settings.setRecentValues(recent);
                    final S3StaticFilesUploader uploader = new S3StaticFilesUploader(settings);
                    uploader.setEventHandler(that);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            UserSettings.save(settings); //saving locally
                            try {
                                uploader.export(fileSelector.getSelectedFiles());
                                if (tracker != null) {
                                    tracker.track();
                                    FolderTracker.save(tracker);// saving locally
                                }
                                JOptionPane.showMessageDialog(rootPane, "Successfully uploaded to S3");
                            } catch (Exception ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(rootPane, "Exception occured while uploading: " + ex.toString());
                            }
                        }
                    }).start();
                } catch (Exception ex) {
                    ex.printStackTrace(System.out);
                    msg = "Exception occured while uploading: " + e.toString();
                    JOptionPane.showMessageDialog(rootPane, "Exception occured while uploading: " + e.toString());
                }
                // now Saving the settings locally
            }
        });
    }

    @Override
    public void uploadComplete(String msg) {
        jTextArea1.append(msg + "\n");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bucketList = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        targetPath = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        sourcePath = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("S3 Files Uploader");

        jLabel1.setText("Select The Bucket");

        jLabel2.setText("Enter Target Path");

        targetPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                targetPathActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("Upload");

        jLabel3.setText("Logs");

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        sourcePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourcePathActionPerformed(evt);
            }
        });

        jLabel4.setText("Select the Source Directory");

        jButton2.setText("Select Source Folder");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(targetPath, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bucketList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(sourcePath, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(520, 520, 520))
                            .addComponent(jScrollPane1))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bucketList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourcePath, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(targetPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sourcePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourcePathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sourcePathActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void targetPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_targetPathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_targetPathActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Main main = new Main();
                main.setVisible(true);
                main.settings = UserSettings.load();
                if (main.settings == null) {
                    main.showLogin();
                } else {
                    main.settings.init();
                    File lastFile = null;
                    try {
                        lastFile = new File(main.settings.getRecentValues().getLocalFolder());
                    } catch (Exception e) {
                    }
                    if (lastFile != null) {
                        main.fileChooser.setSelectedFile(lastFile);
                        main.sourcePath.setText(lastFile.getAbsolutePath());
                    }
                    main.initializeUI();
                    if (lastFile != null) {
                        try {
                            main.tracker = FolderTracker.load(lastFile.getAbsolutePath());
                            main.fileSelector.setLocationRelativeTo(main);
                            main.fileSelector.show(main.tracker);
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                        }
                    }
                }
            }
        });
    }

    private void showLogin() {
        LoginDialog dialog = new LoginDialog(this);
        dialog.setVisible(true);
    }

    private void initializeUI() {
        BaseAmazonS3Client s3Client = settings.getS3Client();
        List<Bucket> buckets = s3Client.listBuckets();
        for (Bucket bucket : buckets) {
            bucketList.addItem(bucket.getName());
        }
        bucketList.setSelectedItem(settings.getRecentValues().getS3Bucket());
        System.out.println(settings.getRecentValues().getS3Bucket());
    }

    public void acceptLogin(String key, String secret) {
        settings = new UserSettings(key, secret);
        UserSettings.save(settings); //saving locally
        initializeUI();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox bucketList;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField sourcePath;
    private javax.swing.JTextField targetPath;
    // End of variables declaration//GEN-END:variables
    private JFileChooser fileChooser = new JFileChooser();
    private FileSelector fileSelector;
    FolderTracker tracker;
}
