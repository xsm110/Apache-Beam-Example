package BeamClickhouseIO.etlToClickhouseIO;



public class AlarmTable {
	/**
	 * 事件ID
	 */
	 private String alarmid;
	 /**
	  * 事件标题
	  */
	 private String alarmTitle;
	 /**
	  * 设备类型
	  */
	 private  String deviceModel;
	 /**
	  * 事件来源
	  */
	 private  Integer alarmSource ;
	 /**
	  * 事件消息
	  */
	 private  String alarmMsg;
	public String getAlarmid() {
		return alarmid;
	}
	public void setAlarmid(String alarmid) {
		this.alarmid = alarmid;
	}
	public String getAlarmTitle() {
		return alarmTitle;
	}
	public void setAlarmTitle(String alarmTitle) {
		this.alarmTitle = alarmTitle;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}




	public Integer getAlarmSource() {
		return alarmSource;
	}
	public void setAlarmSource(Integer alarmSource) {
		this.alarmSource = alarmSource;
	}
	public String getAlarmMsg() {
		return alarmMsg;
	}
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}
	 
}
