package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ENGLISH_WORD")
public class EnglishWord {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;

    @Column(name = "ENGLISH_WORD")
    private String englishWord;

    @Column(name = "WORD_AB")
    private String wordAb;

    @Column(name = "CHINESE_WORD")
    private String chineseWord;

    @Column(name = "OPT_USER")
    private String optUser;

    @Column(name = "OPT_DATE")
    private String optDate;

    @Column(name = "REMARK")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getWordAb() {
        return wordAb;
    }

    public void setWordAb(String wordAb) {
        this.wordAb = wordAb;
    }

    public String getChineseWord() {
        return chineseWord;
    }

    public void setChineseWord(String chineseWord) {
        this.chineseWord = chineseWord;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getOptUser() {
        return optUser;
    }

    public void setOptUser(String optUser) {
        this.optUser = optUser;
    }

    public String getOptDate() {
        return optDate;
    }

    public void setOptDate(String optDate) {
        this.optDate = optDate;
    }
}
