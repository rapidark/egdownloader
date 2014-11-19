package org.arong.egdownloader.ui.work.listenerWork;

import java.util.List;

import javax.swing.JOptionPane;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.ResetAllTaskWindow;
/**
 * 重置任务
 * @author dipoo
 * @since 2014-11-18
 */
public class ResetTaskWork {
	public ResetTaskWork(EgDownloaderWindow mainWindow, List<Task> tasks, String message) {
		//询问是否重置任务
		int result = JOptionPane.showConfirmDialog(null, message);
		if(result == 0){//确定
			
			mainWindow.setEnabled(false);
			ResetAllTaskWindow w = (ResetAllTaskWindow) mainWindow.resetAllTaskWindow;
			if(w == null){
				mainWindow.resetAllTaskWindow = new ResetAllTaskWindow(mainWindow, tasks);
			}
			w = (ResetAllTaskWindow) mainWindow.resetAllTaskWindow;
			w.setInfoLabel("    0");
			w.setVisible(true);
			Task task = null;
			for(int i = 0; i < tasks.size(); i ++){
				task = tasks.get(i);
				if(task.getStatus() != TaskStatus.STARTED//正在下载
						&& task.getStatus() != TaskStatus.UNCREATED//未创建
						 && task.getStatus() != TaskStatus.UNSTARTED){//未开始
					//重置图片状态
					for(int j = 0; j < task.getPictures().size(); j ++){
						task.getPictures().get(j).setCompleted(false);
					}
					task.setCurrent(0);//进度未0
					task.setStatus(TaskStatus.UNSTARTED);//状态未开始
					task.setCompletedTime("");//完成时间置空
					task.setReaded(false);//未阅读
				}
				w.setInfoLabel("    " + (i + 1));
				mainWindow.pictureDbTemplate.update(task.getPictures());
			}
			//保存数据
			mainWindow.taskDbTemplate.update(mainWindow.tasks);
			w.dispose();
			JOptionPane.showMessageDialog(null, "重置任务完成！");
		}
	}
}