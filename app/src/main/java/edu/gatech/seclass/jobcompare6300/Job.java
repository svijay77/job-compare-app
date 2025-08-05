package edu.gatech.seclass.jobcompare6300;

import android.content.Context;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Job implements Serializable {

    private long id;
    private Context context;
    private String title;
    private String company;
    private String location;
    private Integer cOl;
    private BigDecimal salary;
    private BigDecimal bonus;
    private BigDecimal tuition;
    private BigDecimal insurance;
    private BigDecimal discount;
    private BigDecimal adoption;

    private double rank;
    private boolean isCurrentJobFlag = false;

    public Job() {
        title = "";
        company = "";
        location = "";
        cOl = 0;
        salary = BigDecimal.valueOf(0);
        bonus = BigDecimal.valueOf(0);
        tuition = BigDecimal.valueOf(0);
        insurance = BigDecimal.valueOf(0);
        discount = BigDecimal.valueOf(0);
        adoption = BigDecimal.valueOf(0);
    }

    public Job(String _title, String _company, String _loc, Integer _col, BigDecimal _salary, BigDecimal _bonus,
            BigDecimal _tuition, BigDecimal _insurance, BigDecimal _discount, BigDecimal _adoption) {
        title = _title;
        company = _company;
        location = _loc;
        cOl = _col;
        salary = _salary;
        bonus = _bonus;
        tuition = _tuition;
        insurance = _insurance;
        discount = _discount;
        adoption = _adoption;
        validateAndSetInsurance(_salary, _insurance);
    }

    public void setTitle(String newTitle) {
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            title = newTitle;
        } else {
            throw new IllegalArgumentException("Please fill the Title");
        }
    }

    public void setCompany(String companyName) {
        if (companyName != null && !companyName.trim().isEmpty()) {
            company = companyName;
        } else {
            throw new IllegalArgumentException("Please fill the Company");
        }
    }

    public void setLocation(String l) {
        if (l != null && !l.trim().isEmpty()) {
            location = l;
        } else {
            throw new IllegalArgumentException("Please fill the Location");
        }
    }

    public void setCostOfLiving(int c) {
        if (c > 0) {
            cOl = c;
        } else {
            throw new IllegalArgumentException("Please fill the Cost of Living");
        }
    }

    public void setYearlySalary(BigDecimal s) {
        if (s != null && s.compareTo(BigDecimal.ZERO) >= 0) {
            salary = s;
            validateAndSetInsurance(s, insurance);
        } else {
            throw new IllegalArgumentException("Salary has to be a positive number");
        }
    }

    public void setYearlyBonus(BigDecimal b) {
        if (b != null && b.compareTo(BigDecimal.ZERO) >= 0) {
            bonus = b;
        } else {
            throw new IllegalArgumentException("Bonus has to be a positive number");
        }
    }

    public void setTuitionAssistance(BigDecimal t) {
        if (t != null && t.compareTo(BigDecimal.ZERO) >= 0 && t.compareTo(BigDecimal.valueOf(15000)) <= 0) {

            tuition = t;
        } else {
            throw new IllegalArgumentException("Tuition assistance has to be in range");
        }
    }

    public void setInsurance(BigDecimal i) {
        if (i != null) {
            validateAndSetInsurance(salary, i);

        } else {
            throw new IllegalArgumentException("Please fill Insurance");
        }
    }

    private void validateAndSetInsurance(BigDecimal salary, BigDecimal inputInsurance) {
        BigDecimal baseInsurance = inputInsurance != null ? inputInsurance : BigDecimal.ZERO;
        BigDecimal twoPercentSalary = salary.multiply(BigDecimal.valueOf(0.02));
        BigDecimal maxBase = BigDecimal.valueOf(1000);
        BigDecimal calculatedInsurance = twoPercentSalary.add(baseInsurance.min(maxBase));
        if (calculatedInsurance.compareTo(BigDecimal.ZERO) >= 0) {
            insurance = calculatedInsurance;
        } else {
            throw new IllegalArgumentException("Please double check the insurance value");
        }
    }

    public void setAdoptionAssistance(BigDecimal a) {
        if (a != null && a.compareTo(BigDecimal.ZERO) >= 0 && a.compareTo(BigDecimal.valueOf(30000)) <= 0) {
            adoption = a;

        } else {
            throw new IllegalArgumentException("Adoption Assistance is out of range");
        }
    }

    public void setJobScore(double r) {
        rank = r;
    }

    public void setIsCurrentJob(boolean isCurrent) {
        isCurrentJobFlag = isCurrent;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public double getJobScore() {
        return rank;
    }

    public BigDecimal getYearlySalary() {
        return salary;
    }

    public BigDecimal getYearlyBonus() {
        return bonus;
    }

    public boolean isCurrentJob() {
        return isCurrentJobFlag;
    }

    public Integer getCostOfLiving() {
        return cOl;
    }

    public BigDecimal getTuitionAssistance() {
        return tuition;
    }

    public BigDecimal getInsurance() {
        return insurance;
    }

    public BigDecimal getEmployeeDiscount() {
        return discount;
    }

    public BigDecimal getAdoptionAssistance() {
        return adoption;
    }

    public void setId(long aLong) {
        id = aLong;
    }



    public double calculateRank() {
        //List<Job> jobs = loadJobsFromDatabase();
        User user = new User(context);
        Settings Settings = user.getSettings();


        int weightSalary = (Settings != null)? Settings.getSalaryWeight() : 1;
        int weightBonus = (Settings != null)? Settings.getBonusWeight() : 1;
        int weightTuition = (Settings != null)? Settings.getTuitionWeight() : 1;
        int weightHealth = (Settings != null)? Settings.getHealthWeight() : 1;
        int weightDiscount = (Settings != null)? Settings.getEmployeeWeight() : 1;
        int weightAdoption = (Settings != null)? Settings.getAdoptionWeight() : 1;


        weightSalary = Math.max(0 , Math.min(9,weightSalary));
        weightBonus = Math.max(0 , Math.min(9,weightBonus));
        weightTuition = Math.max(0 , Math.min(9,weightTuition));
        weightHealth = Math.max(0 , Math.min(9,weightHealth));
        weightDiscount = Math.max(0 , Math.min(9,weightDiscount));
        weightAdoption = Math.max(0 , Math.min(9,weightAdoption));






        Integer total = weightSalary + weightBonus + weightTuition + weightHealth + weightDiscount + weightAdoption;
        if (total == 0) total = 6;

        BigDecimal weightAYS = BigDecimal.valueOf(weightSalary).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal weightAYB = BigDecimal.valueOf(weightBonus).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal weightTR = BigDecimal.valueOf(weightTuition).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal weightHI = BigDecimal.valueOf(weightHealth).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal weightEPSD = BigDecimal.valueOf(weightDiscount).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal weightCAA = BigDecimal.valueOf(weightAdoption).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal weightAYB = BigDecimal.valueOf(comparisonSettings.getBonusWeight() / total);
//        BigDecimal weightTR = BigDecimal.valueOf(comparisonSettings.getTuitionWeight() / total);
//        BigDecimal weightHI = BigDecimal.valueOf(comparisonSettings.getHealthWeight() / total);
//        BigDecimal weightEPSD = BigDecimal.valueOf(comparisonSettings.getEmployeeWeight() / total);
//        BigDecimal weightCAA = BigDecimal.valueOf(comparisonSettings.getAdoptionWeight() / total);

        // AYS = Yearly Salary Adjusted for cost of living
        BigDecimal ays = salary.divide(BigDecimal.valueOf(cOl), 2, BigDecimal.ROUND_HALF_UP);
        // AYB = Yearly Bonus Adjusted for cost of living
        BigDecimal ayb = bonus.divide(BigDecimal.valueOf(cOl), 2, BigDecimal.ROUND_HALF_UP);
        // TR = Tuition Reimbursement ($0 to $15,000 inclusive annually)
        // HI = Health Insurance ($0-$1,000 inclusive + 2% of AYS annually)
        // EPSD = Employee Product/Service Discount (dollar amount up to 18% of Yearly
        // Salary)
        // CAA = Child Adoption Assistance (expressed as a lump sum available over 5
        // years)
        BigDecimal caaPerYear = adoption.divide(BigDecimal.valueOf(5), 5, BigDecimal.ROUND_HALF_UP);


//        BigDecimal weightedAYS = ays.multiply(weightAYS);
//        BigDecimal weightedAYB = ayb.multiply(weightAYB);
//        BigDecimal weightedTR = tuition.multiply(weightTR);
//        BigDecimal weightedHI = insurance.multiply(weightHI);
//        BigDecimal weightedEPSD = discount.multiply(weightEPSD);
//        BigDecimal weightedCAA = caaPerYear.multiply(weightCAA);
//
//        BigDecimal js = weightedAYS.add(weightedAYB).add(weightedTR).add(weightedHI).add(weightedEPSD).add(weightedCAA);


        BigDecimal js = BigDecimal.ZERO;
        js = js.add(ays.multiply(weightAYS));
        js = js.add(ayb.multiply(weightAYB));
        js = js.add(tuition.multiply(weightTR));
        js = js.add(insurance.multiply(weightHI));
        js = js.add(discount.multiply(weightEPSD));
        js = js.add(caaPerYear.multiply(weightCAA));

        rank = js.doubleValue();
        return rank;

    }

    public int compareTo(Job _job) {
        if (this.getJobScore() > _job.getJobScore()) {
            return 1;
        } else if (this.getJobScore() == _job.getJobScore()) {
            return 0;
        } else {
            return -1;
        }
    }
}
