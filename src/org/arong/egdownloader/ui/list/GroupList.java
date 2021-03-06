package org.arong.egdownloader.ui.list;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.arong.egdownloader.db.impl.PictureDom4jDbTemplate;
import org.arong.egdownloader.db.impl.SettingDom4jDbTemplate;
import org.arong.egdownloader.db.impl.TaskDom4jDbTemplate;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.swing.AJMenuItem;
import org.arong.egdownloader.ui.swing.AJPopupMenu;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.GroupWindow;
import org.arong.egdownloader.ui.window.InitWindow;
import org.arong.util.FileUtil;
/**
 * 任务组列表
 * @author dipoo
 * @since 2015-01-07
 */
public class GroupList extends JList {

	private static final long serialVersionUID = -7702879865264332528L;
	
	private JPopupMenu popupMenu;
	private JMenuItem deleteMenu = new AJMenuItem("删除", new Color(0,0,85), IconManager.getIcon("delete"), null);
	
	public GroupList(List<File> groups, final GroupWindow window, final EgDownloaderWindow mainWindow){
		this.setModel(new GroupListModel(groups));
		//this.setCellRenderer(new GroupListCellReader());
		this.setCursor(CursorManager.getPointerCursor());
		this.setForeground(Color.BLUE);
		
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent me) {
				int clickNum = me.getClickCount();
				GroupList list = (GroupList)me.getSource();
				//左键
				if(me.getButton() == MouseEvent.BUTTON1){
					//双击
					if(2 == clickNum){
						String groupName = "";
						if(list.getSelectedIndex() != 0){
							groupName = list.getSelectedValue().toString();
							if(groupName.indexOf("|") != -1){
								groupName = groupName.substring(groupName.indexOf("|") + 1, groupName.length()).trim();
							}
						}
						//如果是在主界面切换的任务组，且选择的任务组不变，则关闭任务组列表窗口
						if(mainWindow != null && groupName.equals(ComponentConst.groupName)){
							window.dispose();
							return;
						}
						if(mainWindow != null){
							//保存前一个任务组的数据
							mainWindow.saveTaskGroupData();
							mainWindow.dispose();
						}
						ComponentConst.groupName = groupName;
						ComponentConst.changeDataPath(groupName);
						ComponentConst.changeDataXmlPath();
						window.dispose();
						if(mainWindow != null){
							/**
							 * 更新dom
							 */
							SettingDom4jDbTemplate.updateDom();
							TaskDom4jDbTemplate.updateDom();
							PictureDom4jDbTemplate.updateDom();
						}
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								new InitWindow();
							}
						});
					}
				}//右键
				else if(me.getButton() == MouseEvent.BUTTON3){
					int rowIndex = list.locationToIndex(me.getPoint());
					//使之选中
					list.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
					if(popupMenu == null){
						popupMenu = new AJPopupMenu(deleteMenu);
					}
					popupMenu.show(list, me.getPoint().x, me.getPoint().y);
				}
				
			}
		});
		final GroupList this_ = this;
		deleteMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(this_.getSelectedIndex() == 0){
					JOptionPane.showMessageDialog(this_, "默认空间不能删除");
				}else{
					String name = this_.getSelectedValue().toString();
					if(name.indexOf("|") != -1){
						name = name.substring(name.indexOf("|") + 1, name.length()).trim();
					}
					int r = JOptionPane.showConfirmDialog(this_, "删除任务组将会删除已下载的漫画，且不可恢复，您确定要删除任务组：" + name + "吗？");
					if(r == JOptionPane.OK_OPTION){
						//如果是在主界面切换的任务组，且选择的任务组不变，则关闭主窗口
						if(mainWindow != null && name.equals(ComponentConst.groupName)){
							mainWindow.saveTaskGroupData();
							mainWindow.dispose();
						}
						File file = new File(ComponentConst.ROOT_DATA_PATH + "/" + name);
						if(file.exists()){
							FileUtil.deleteFile(file);
						}
						File dataFile = new File(ComponentConst.ROOT_DATA_PATH);
						if(!dataFile.exists()){
							dataFile.mkdirs();
							if(window != null){
								window.dispose();
							}
							ComponentConst.groupName = "";
							ComponentConst.changeDataPath("");
							ComponentConst.changeDataXmlPath();
							/**
							 * 更新dom
							 */
							SettingDom4jDbTemplate.updateDom();
							TaskDom4jDbTemplate.updateDom();
							PictureDom4jDbTemplate.updateDom();
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									new InitWindow();
								}
							});
						}else{
							File[] files = dataFile.listFiles();
							List<File> groups = new ArrayList<File>();
							for(File file2 : files){
								if(file2.isDirectory()){
									groups.add(file2);
								}
							}
							if(groups.size() > 0){
								this_.setModel(new GroupListModel(groups));
								this_.updateUI();
							}else{
								if(window != null){
									window.dispose();
								}
								ComponentConst.groupName = "";
								ComponentConst.changeDataPath("");
								ComponentConst.changeDataXmlPath();
								/**
								 * 更新dom
								 */
								SettingDom4jDbTemplate.updateDom();
								TaskDom4jDbTemplate.updateDom();
								PictureDom4jDbTemplate.updateDom();
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										new InitWindow();
									}
								});
							}
						}
						
					}
				}
			}
		});
	}
}
