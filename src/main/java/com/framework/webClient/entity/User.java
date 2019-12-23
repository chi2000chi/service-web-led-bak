package com.framework.webClient.entity;
import java.io.Serializable;

public class User implements Serializable{

		/**
	 * @Fields serialVersionUID : 序列化
	 */
	private static final long serialVersionUID = 1L;
		private int userid;
        private String name;
        private String pass;


		public int getUserid() {
			return userid;
		}
		public void setUserid(int userid) {
			this.userid = userid;
		}
		public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getPass() {
            return pass;
        }
        public void setPass(String pass) {
            this.pass = pass;
        }
}