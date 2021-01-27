package com.yidiantong.bean;

public class VersionBean {

    /**
     * code : 0
     * message : 成功
     * data : {"version":"2.2","info":"更新内容：xxxx","fileSize":42.74,"downloadUrl":"http://47.101.172.44:3002/download"}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * version : 2.2
         * info : 更新内容：xxxx
         * fileSize : 42.74
         * downloadUrl : http://47.101.172.44:3002/download
         */

        private String version;
        private String info;
        private double fileSize;
        private String downloadUrl;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public double getFileSize() {
            return fileSize;
        }

        public void setFileSize(double fileSize) {
            this.fileSize = fileSize;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }
    }
}
