package client;
import server.ServerGUI;
import server.ServerThread;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.awt.Font;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;

public class ClientLoginGUI extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	static Map<String,ClientServerThread> serverThread = new ConcurrentHashMap<>();

	JTextField UserName;// 用户名文本框
	JPasswordField UserPassword;// 密码文本框
	JTextField port;

	private JLabel pictureLabel;// 图片
	private JLabel iconLabel;// 图标
	private JLabel userNameLabel;// 用户名标签
	private JLabel userPasswordLabel;// 密码标签

	private JButton loginButton;// 登录按钮
	private JButton registerButton;// 注册按钮

	Socket socket = null;

	public ClientLoginGUI() throws Exception {
		this.setTitle("聊一聊");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setSize(420, 320);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		// 设置背景
		pictureLabel = new JLabel();
		Image imgbackground = new ImageIcon("F:/pictures/background.jpg").getImage();
		pictureLabel.setIcon(new ImageIcon(imgbackground));
		pictureLabel.setSize(420, 150);
		getContentPane().add(pictureLabel);

		// 设置头像
		iconLabel = new JLabel();
		Image imgiconlabel = new ImageIcon("F:/pictures/icon.png").getImage();
		iconLabel.setIcon(new ImageIcon(imgiconlabel));
		iconLabel.setBounds(40, 155, 96, 96);
		this.add(iconLabel);

		// 设置输入框
		port = new JTextField();
		port.setFont(new Font("华文楷体", Font.PLAIN, 15));
		port.setBounds(230, 140, 150, 30);
		this.add(port);
		JLabel portPanel = new JLabel("我的端口");
		portPanel.setFont(new Font("华文隶书", Font.PLAIN, 15));
		portPanel.setBounds(150, 140, 70, 20);
		this.add(portPanel);

		// 设置输入框
		UserName = new JTextField();
		UserName.setFont(new Font("华文楷体", Font.PLAIN, 15));
		UserName.setBounds(230, 170, 150, 30);
		this.add(UserName);
		userNameLabel = new JLabel("用户名");
		userNameLabel.setFont(new Font("华文隶书", Font.PLAIN, 15));
		userNameLabel.setBounds(150, 170, 70, 20);
		this.add(userNameLabel);

		UserPassword = new JPasswordField();
		UserPassword.setFont(new Font("SimSun", Font.PLAIN, 12));
		UserPassword.setBounds(230, 200, 150, 30);
		this.add(UserPassword);
		userPasswordLabel = new JLabel("密码");
		userPasswordLabel.setFont(new Font("华文隶书", Font.PLAIN, 15));
		userPasswordLabel.setBounds(150, 200, 70, 20);
		this.add(userPasswordLabel);

		// 设置登录注册按钮
		loginButton = new JButton("登录");
		loginButton.setFont(new Font("华文行楷", Font.PLAIN, 20));
		loginButton.setBounds(150, 250, 150, 30);
		loginButton.setForeground(Color.blue);
		loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置光标
		this.add(loginButton);

		registerButton = new JButton("注册");
		registerButton.setFont(new Font("华文行楷", Font.PLAIN, 15));
		registerButton.setBounds(344, 260, 70, 30);
		loginButton.setForeground(Color.blue);
		registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(registerButton);

		this.setVisible(true);

		// 登录按钮添加事件
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cmd = e.getActionCommand();
				if ("登录".equals(cmd)) {
					try {
						if (!port.getText().isEmpty() &&!UserName.getText().isEmpty() && !new String(UserPassword.getPassword()).isEmpty()) {
							try {
								socket = new Socket();
								SocketAddress sAddr = new InetSocketAddress("127.0.0.1",6000);
								socket.connect(sAddr, 8000);
							} catch (Exception e1) {
								socket.close();
								e1.printStackTrace();
							}
							if (socket.isClosed()) {
								JOptionPane.showMessageDialog(null, "服务器已关闭！", "提示", JOptionPane.PLAIN_MESSAGE);
								return;
							}
							DataOutputStream out = new DataOutputStream(socket.getOutputStream());
							DataInputStream in = new DataInputStream(socket.getInputStream());
							out.writeUTF(cmd + "-1_~" + UserName.getText() + "~2/-"
									+ new String(UserPassword.getPassword())+"~2/-"+port.getText());
							out.flush();
							String str = in.readUTF();
							if (str.equals("登录成功！")) {
								ClientLoginGUI.this.dispose();
								new ClientThread(socket, UserName.getText(),port.getText());
							}
							JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.PLAIN_MESSAGE);
							if (str.equals("用户已经在线，不能重复登陆！")) {
								socket.close();
							}
						} else if (UserName.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "请输入用户名！", "提示", JOptionPane.PLAIN_MESSAGE);
						} else if(UserPassword.getPassword()==null || UserPassword.getPassword().length==0){
							JOptionPane.showMessageDialog(null, "请输入密码！", "提示", JOptionPane.PLAIN_MESSAGE);
						}else
						JOptionPane.showMessageDialog(null, "请输入端口号！", "提示", JOptionPane.PLAIN_MESSAGE);
					} catch (Exception e1) {
						try {
							socket.close();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
						e1.printStackTrace();
					}

				}
			}
		});
		// 注册按钮添加事件
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cmd = e.getActionCommand();
				if ("注册".equals(cmd)) {
					try {
						new ClientRegisterGUI();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
}
