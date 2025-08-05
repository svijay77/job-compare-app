package edu.gatech.seclass.jobcompare6300;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Used to create some dummy data until we hook up the rest
// of our functionality
public class MockData {

    public static List<Job> getMockJobOffers() {
        List<Job> jobs = new ArrayList<>();

        Job job1 = new Job();
        job1.setTitle("Software Engineer");
        job1.setCompany("Google");
        job1.setLocation("Mountain View, CA");
        job1.setCostOfLiving(180);
        job1.setYearlySalary(new BigDecimal(150000.00));
        job1.setYearlyBonus(new BigDecimal(30000.00));
        job1.setJobScore(87.5);

        Job job2 = new Job();
        job2.setTitle("Mobile Developer");
        job2.setCompany("Apple");
        job2.setLocation("Cupertino, CA");
        job2.setCostOfLiving(190);
        job2.setYearlySalary(new BigDecimal(160000.00));
        job2.setYearlyBonus(new BigDecimal(25000.00));
        job2.setJobScore(85.2);

        Job job3 = new Job();
        job3.setTitle("Data Scientist");
        job3.setCompany("Amazon");
        job3.setLocation("Seattle, WA");
        job3.setCostOfLiving(150);
        job3.setYearlySalary(new BigDecimal(140000.00));
        job3.setYearlyBonus(new BigDecimal(20000.00));
        job3.setJobScore(81.7);

        Job job4 = new Job();
        job4.setTitle("Product Manager");
        job4.setCompany("Microsoft");
        job4.setLocation("Redmond, WA");
        job4.setCostOfLiving(145);
        job4.setYearlySalary(new BigDecimal(145000.00));
        job4.setYearlyBonus(new BigDecimal(30000.00));
        job4.setJobScore(90.1);

        Job job5 = new Job();
        job5.setTitle("UX Designer");
        job5.setCompany("Meta");
        job5.setLocation("Menlo Park, CA");
        job5.setCostOfLiving(175);
        job5.setYearlySalary(new BigDecimal(135000.00));
        job5.setYearlyBonus(new BigDecimal(27000.00));
        job5.setJobScore(83.9);

        jobs.add(job1);
        jobs.add(job2);
        jobs.add(job3);
        jobs.add(job4);
        jobs.add(job5);

        return jobs;
    }

    public static Job getCurrentJob() {
        Job currentJob = new Job();
        currentJob.setTitle("Senior Developer");
        currentJob.setCompany("Current Company");
        currentJob.setLocation("Atlanta, GA");
        currentJob.setCostOfLiving(110);
        currentJob.setYearlySalary(new BigDecimal(120000.00));
        currentJob.setYearlyBonus(new BigDecimal(15000.00));
        currentJob.setJobScore(75.8);
        return currentJob;
    }
}