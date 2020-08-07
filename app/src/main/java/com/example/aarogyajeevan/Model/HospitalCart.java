package com.example.aarogyajeevan.Model;

public class HospitalCart {
        private String pid, Hname, price, quantity, discount,Hlocation;

        public HospitalCart() {
        }

        public HospitalCart(String pid, String hname, String price, String quantity, String discount, String hlocation) {
            this.pid = pid;
            this.Hname = hname;
            this.price = price;
            this.quantity = quantity;
            this.discount = discount;
            this.Hlocation = hlocation;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getHname() {
            return Hname;
        }

        public void setHname(String hname) {
            this.Hname = hname;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getHlocation() {
            return Hlocation;
        }

        public void setHlocation(String hlocation) {
            this.Hlocation = hlocation;
        }
    }
