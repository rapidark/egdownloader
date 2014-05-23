package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MenuMouseListener;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJMenu;
import org.arong.egdownloader.ui.swing.AJMenuBar;
import org.arong.egdownloader.ui.swing.AJTextArea;
import org.arong.egdownloader.ui.window.form.AddFormDialog;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.egdownloader.version.Version;

/**
 * 主线程类
 * @author 阿荣
 * @since 2014-05-21
 */
public class EgDownloaderWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 8904976570969033245L;
	
	JMenuBar jMenuBar;//菜单栏
	public JFrame toolsMenuWindow;
	public JDialog aboutMenuWindow;
	public JDialog addFormWindow;
	
	
	JTextArea statusTextarea;//状态显示
	
	public EgDownloaderWindow(){
		// 设置主窗口
		this.setTitle(Version.NAME);
		this.setIconImage(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + "down.png")).getImage());
		this.getContentPane().setLayout(null);
		this.setSize(ComponentConst.CLIENT_WIDTH, ComponentConst.CLIENT_HEIGHT);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// 设置菜单
		JMenu newTaskMenu = new AJMenu(ComponentConst.ADD_MENU_TEXT,
				ComponentConst.SETTING_MENU_NAME, "add.gif", new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
					public void doWork(Window mainWindow) {
						EgDownloaderWindow this_ = (EgDownloaderWindow)mainWindow;
						if(this_.addFormWindow == null){
							this_.addFormWindow = new AddFormDialog(this_);
						}else{
							if(!this_.addFormWindow.isVisible()){//如果是隐藏的，则清空文本
								((AddFormDialog)this_.addFormWindow).emptyField();
							}
							this_.addFormWindow.setVisible(true);
						}
//						this_.setVisible(false);
						this_.disable();
					}
				}));
		JMenu deleteTasksMenu = new AJMenu(ComponentConst.DELETE_MENU_TEXT,
				ComponentConst.TOOLS_MENU_NAME, "delete.gif",  new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
					public void doWork(Window mainWindow) {
						//EgDownloaderWindow this_ = (EgDownloaderWindow)mainWindow;
						
					}
				}));
		
		MouseListener menuMouseListener = new MenuMouseListener(this);
		JMenu settingMenu = new AJMenu(ComponentConst.SETTING_MENU_TEXT,
				ComponentConst.SETTING_MENU_NAME, "cog.png", menuMouseListener);
		JMenu toolsMenu = new AJMenu(ComponentConst.TOOLS_MENU_TEXT,
				ComponentConst.TOOLS_MENU_NAME, "tool.png", menuMouseListener);
		JMenu aboutMenu = new AJMenu(ComponentConst.ABOUT_MENU_TEXT,
				ComponentConst.ABOUT_MENU_NAME, "user.png", menuMouseListener);

		// 构造菜单栏并添加菜单
		jMenuBar = new AJMenuBar(0, 0, ComponentConst.CLIENT_WIDTH, 30, newTaskMenu, deleteTasksMenu, settingMenu, toolsMenu,
				aboutMenu);
		
		
		//状态栏
		statusTextarea = new AJTextArea("当前无任务", 0, ComponentConst.CLIENT_HEIGHT - 58, ComponentConst.CLIENT_WIDTH, 25);
		statusTextarea.setForeground(Color.BLACK);
		statusTextarea.setBackground(new Color(230,230,230));
		// 添加各个子组件
		ComponentUtil.addComponents(getContentPane(), jMenuBar, statusTextarea);
	}

	public void actionPerformed(ActionEvent e) {

	}
}
