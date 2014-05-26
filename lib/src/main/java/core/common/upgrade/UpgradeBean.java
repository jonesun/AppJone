package core.common.upgrade;

/**
 * Created by jone_admin on 13-12-12.
 */
public class UpgradeBean {
    private String formUrl; //来自的URL
    private String downloadSavePath; //下载保存的路径
    private String downloadSaveFileName; //下载保存的文件名
    private boolean isManualUpgrade; //是否手动升级

    public UpgradeBean(){}
    public UpgradeBean(String formUrl){
        this.formUrl = formUrl;
    }
    public UpgradeBean(String formUrl, String downloadSavePath, String downloadSaveFileName){
        this.formUrl = formUrl;
        this.downloadSavePath = downloadSavePath;
        this.downloadSaveFileName = downloadSaveFileName;
    }
    public UpgradeBean(String formUrl, String downloadSavePath, String downloadSaveFileName, boolean isManualUpgrade){
        this.formUrl = formUrl;
        this.downloadSavePath = downloadSavePath;
        this.downloadSaveFileName = downloadSaveFileName;
        this.isManualUpgrade = isManualUpgrade;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public String getDownloadSavePath() {
        return downloadSavePath;
    }

    public void setDownloadSavePath(String downloadSavePath) {
        this.downloadSavePath = downloadSavePath;
    }

    public String getDownloadSaveFileName() {
        return downloadSaveFileName;
    }

    public void setDownloadSaveFileName(String downloadSaveFileName) {
        this.downloadSaveFileName = downloadSaveFileName;
    }

    public boolean isManualUpgrade() {
        return isManualUpgrade;
    }

    public void setManualUpgrade(boolean isManualUpgrade) {
        this.isManualUpgrade = isManualUpgrade;
    }
}
