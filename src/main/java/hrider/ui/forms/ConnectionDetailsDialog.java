package hrider.ui.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import hrider.config.ConnectionDetails;
import hrider.config.GlobalConfig;
import hrider.config.ServerDetails;
import hrider.hbase.Connection;
import hrider.hbase.ConnectionManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Copyright (C) 2012 NICE Systems ltd.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Igor Cher
 * @version %I%, %G%
 */
public class ConnectionDetailsDialog extends JDialog {

    //region Variables
    private JPanel            contentPane;
    private JButton           buttonConnect;
    private JButton           buttonCancel;
    private JTextField        hbaseServer;
    private JTextField        zooKeeperServer;
    private JSpinner          zooKeeperPort;
    private JSpinner          hbasePort;
    private ConnectionDetails connectionDetails;
    //endregion

    //region Constructor
    public ConnectionDetailsDialog() {
        setContentPane(this.contentPane);
        setModal(true);
        setTitle("Connect to an Hbase server...");
        getRootPane().setDefaultButton(this.buttonConnect);

        this.hbasePort.setValue(GlobalConfig.instance().get(Integer.class, "connection.hbase.defaultPort", "9000"));
        this.zooKeeperPort.setValue(GlobalConfig.instance().get(Integer.class, "connection.zookeeper.defaultPort", "2181"));

        this.buttonConnect.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onOK();
                }
            });

        this.buttonCancel.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onCancel();
                }
            });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    onCancel();
                }
            });

        // call onCancel() on ESCAPE
        this.contentPane.registerKeyboardAction(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onCancel();
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.hbaseServer.getDocument().addDocumentListener(
            new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    ConnectionDetailsDialog.this.zooKeeperServer.setText(ConnectionDetailsDialog.this.hbaseServer.getText());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    ConnectionDetailsDialog.this.zooKeeperServer.setText(ConnectionDetailsDialog.this.hbaseServer.getText());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    ConnectionDetailsDialog.this.zooKeeperServer.setText(ConnectionDetailsDialog.this.hbaseServer.getText());
                }
            });
    }
    //endregion

    //region Public Methods
    public void showDialog(Component owner) {
        this.setComponentOrientation(owner.getComponentOrientation());
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(owner);
        this.setVisible(true);
    }

    public ConnectionDetails getConnectionDetails() {
        return this.connectionDetails;
    }
    //endregion

    //region Private Methods
    private void onOK() {
        this.contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        this.connectionDetails = new ConnectionDetails() {{
            setHbaseServer(
                new ServerDetails(ConnectionDetailsDialog.this.hbaseServer.getText(), ConnectionDetailsDialog.this.hbasePort.getValue().toString()));
            setZookeeperServer(
                new ServerDetails(
                    ConnectionDetailsDialog.this.zooKeeperServer.getText(), ConnectionDetailsDialog.this.zooKeeperPort.getValue().toString()));
        }};

        try {

            ConnectionManager.create(this.connectionDetails);

            GlobalConfig.instance().set("connection.zookeeper.defaultPort", this.zooKeeperPort.getValue().toString());
            GlobalConfig.instance().set("connection.hbase.defaultPort", this.hbasePort.getValue().toString());
            GlobalConfig.instance().save();

            dispose();
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, String.format(
                "%s\n\nMake sure you have access to all nodes of the cluster you try\nto connect to. In case you don't, map the nodes in your hosts file.",
                ex.getMessage()), "Failed to connect...", JOptionPane.ERROR_MESSAGE);

            ConnectionManager.release(this.connectionDetails);
            this.connectionDetails = null;
        }
        finally {
            this.contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void onCancel() {
        dispose();
    }
    //endregion

    {
        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(5, 0, 0, 0), -1, -1));
        contentPane.add(
            panel1, new GridConstraints(
            1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(
            panel2, new GridConstraints(
            1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonConnect = new JButton();
        buttonConnect.setText("Connect");
        panel2.add(
            buttonConnect, new GridConstraints(
            0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(
            buttonCancel, new GridConstraints(
            0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel1.add(
            separator1, new GridConstraints(
            0, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(
            panel3, new GridConstraints(
            0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Hbase host:");
        panel3.add(
            label1, new GridConstraints(
            0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null,
            null, 0, false));
        hbaseServer = new JTextField();
        panel3.add(
            hbaseServer, new GridConstraints(
            0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED,
            null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Zoo Keeper host:");
        panel3.add(
            label2, new GridConstraints(
            1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null,
            null, 0, false));
        zooKeeperServer = new JTextField();
        panel3.add(
            zooKeeperServer, new GridConstraints(
            1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED,
            null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Port:");
        panel3.add(
            label3, new GridConstraints(
            0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null,
            null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Port:");
        panel3.add(
            label4, new GridConstraints(
            1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null,
            null, 0, false));
        hbasePort = new JSpinner();
        panel3.add(
            hbasePort, new GridConstraints(
            0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED,
            null, null, null, 0, false));
        zooKeeperPort = new JSpinner();
        panel3.add(
            zooKeeperPort, new GridConstraints(
            1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED,
            null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
