package com.yjy.web.mms.model.entity.process;

import javax.persistence.*;

/**
 * @Author:winper001 2020-02-17 10:59
 **/

@Entity
@Table(name="aoa_passportCertificate")
public class PassportCertificate {

    @Id
    @Column(name="printId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long printId;


    @Column(name="process_id")
    private Long processId;


    @Column(name="workTime")
    private Long workTime;//工作年限

    @Column(name="holidayTime")//总假期
    private Long holidayTime;

    @Column(name="winterVacation")//年休假
    private Long winterVacation;

    @Column(name="sabbatical")//公休假
    private Long sabbatical;


    @Column(name="accumulateTime")//积假
    private Long accumulateTime;


    @Column(name="passportId")//护照号
    private String passportId;

    @Column(name="birthYMD")//申请人的出生年月日
    private String birthYMD;

    public Long getPrintId() {
        return printId;
    }

    public void setPrintId(Long printId) {
        this.printId = printId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Long workTime) {
        this.workTime = workTime;
    }

    public Long getHolidayTime() {
        return holidayTime;
    }

    public void setHolidayTime(Long holidayTime) {
        this.holidayTime = holidayTime;
    }

    public Long getWinterVacation() {
        return winterVacation;
    }

    public void setWinterVacation(Long winterVacation) {
        this.winterVacation = winterVacation;
    }

    public Long getSabbatical() {
        return sabbatical;
    }

    public void setSabbatical(Long sabbatical) {
        this.sabbatical = sabbatical;
    }

    public Long getAccumulateTime() {
        return accumulateTime;
    }

    public void setAccumulateTime(Long accumulateTime) {
        this.accumulateTime = accumulateTime;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getBirthYMD() {
        return birthYMD;
    }

    public void setBirthYMD(String birthYMD) {
        this.birthYMD = birthYMD;
    }

    @Override
    public String toString() {
        return "PassportCertificate{" +
                "printId=" + printId +
                ", processId=" + processId +
                ", workTime=" + workTime +
                ", holidayTime=" + holidayTime +
                ", winterVacation=" + winterVacation +
                ", sabbatical=" + sabbatical +
                ", accumulateTime=" + accumulateTime +
                ", passportId='" + passportId + '\'' +
                ", birthYMD='" + birthYMD + '\'' +
                '}';
    }
}
