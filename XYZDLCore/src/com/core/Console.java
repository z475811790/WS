package com.core;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import com.core.event.IEventHandler;
import com.core.event.XEvent;
import com.core.util.StringUtil;

/**
 * @author xYzDl
 * @date 2018年2月27日 下午11:40:32
 * @description 控制台面板
 */
public class Console extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6480389156911223113L;

	private int inputAreaHeight = 25;
	private JList<String> list = new JList<>();
	private DefaultListModel<String> model = new DefaultListModel<>();
	private JTextField tf = new JTextField();
	private JScrollPane scrollPane;
	private InvokeLater invokeLater;

	public IEventHandler onInputHandler;

	private static Console _singleton;

	public static Console singleton() {
		if (_singleton == null)
			_singleton = new Console();
		return _singleton;
	}

	public Console() {
		super(null);
		list.setFont(new Font("宋体", Font.PLAIN, 14));
		list.setModel(model);
		scrollPane = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		tf.setSize(600, inputAreaHeight);
		tf.setFont(new Font("宋体", Font.PLAIN, 14));

		class KeyEnter extends KeyAdapter {
			@Override
			public void keyPressed(KeyEvent e) {
				if (onInputHandler != null)
					try {
						onInputHandler.execute(new XEvent(e));
					} catch (Exception exception) {
						exception.printStackTrace();
					}
			}
		}

		tf.addKeyListener(new KeyEnter());

		this.add(tf);
		this.add(scrollPane);

		class ComponentResized extends ComponentAdapter {
			@Override
			public void componentResized(ComponentEvent e) {
				Component component = e.getComponent();
				scrollPane.setSize(component.getWidth(), component.getHeight() - inputAreaHeight);
				tf.setBounds(0, component.getHeight() - inputAreaHeight, component.getWidth(), inputAreaHeight);
			}
		}

		this.addComponentListener(new ComponentResized());

		invokeLater = new InvokeLater();
	}

	public void addContent(String content) {
		if (content.length() > 60) {
			model.addElement(content.substring(0, 60));
			model.addElement(content.substring(60, content.length()));
		} else {
			model.addElement(content);
		}

		SwingUtilities.invokeLater(invokeLater);// 解决渲染延时的问题
	}

	public static void addMsg(String msg) {
		singleton().addContent(msg);
	}

	public void onInput(XEvent e) {
		KeyEvent event = (KeyEvent) e.data;
		if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			JTextField tf = (JTextField) event.getComponent();
			if (StringUtil.isNullOrWhiteSpaceOrEmpty(tf.getText()))
				return;
			addContent(tf.getText());
			tf.setText("");
		}
	}

	class InvokeLater implements Runnable {
		@Override
		public void run() {
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		}
	}
}
