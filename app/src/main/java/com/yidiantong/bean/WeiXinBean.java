package com.yidiantong.bean;

import java.io.Serializable;

public class WeiXinBean implements Serializable {

    /**
     * code : 0
     * message : 头像上传成功
     * data : {"path":"http://zhengyongqi.oss-cn-hangzhou.aliyuncs.com/yidiantong/20201228/79965096-72cf-4f5d-8535-98d3fb744c2d.bin"}
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
         * path : http://zhengyongqi.oss-cn-hangzhou.aliyuncs.com/yidiantong/20201228/79965096-72cf-4f5d-8535-98d3fb744c2d.bin
         */

        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
