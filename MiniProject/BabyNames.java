import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
public class BabyNames {
    
    public void totalBirths (FileResource fr){
        int totalBirth = 0, totalFemale = 0, totalMale = 0, femaleCtr = 0, maleCtr = 0;
        for(CSVRecord rec : fr.getCSVParser(false)){
            String name = rec.get(0);
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirth += numBorn;
            if(rec.get(1).equals("F")){
                totalFemale += numBorn;
                System.out.println(name + " " +numBorn);
                femaleCtr++;
            }
            else if(rec.get(1).equals("M")){
                totalMale += numBorn;
                System.out.println(name + " " +numBorn);
                maleCtr++;
            }
        }
        System.out.println("total births: "+totalBirth);
        System.out.println("total Females: "+totalFemale);
        System.out.println("total Males: "+totalMale);
        System.out.println("total counter for female: "+femaleCtr);
        System.out.println("total counter for male: "+maleCtr);
    }
    
    public void testTotalBirths(){
        FileResource fr = new FileResource();
        totalBirths(fr);
    }
    
    public int getRank(int year, String name, String gender){
        //return the rank of the name for the given gender
        FileResource fr = new FileResource("F:/Productivity/Java/us_babynames/us_babynames_by_year/yob"+year+".csv");
        int rank = 0, femaleCtr = 0, maleCtr = 0;
        for(CSVRecord rec : fr.getCSVParser(false)){
            String currName = rec.get(0);
            if(rec.get(1).equals("F")){
                femaleCtr++;
                if(rec.get(0).equals(name)&&rec.get(1).equals(gender)){
                    rank = femaleCtr;
                    break;
                }
                else{
                    rank = -1;
                }
            }
            else if(rec.get(1).equals("M")){
                maleCtr++;
                if(rec.get(0).equals(name)&&rec.get(1).equals(gender)){
                    rank = maleCtr;
                    break;
                }
                else{
                    rank = -1;
                }
            }
            else{
                rank = -1;
            }
        }
        return rank;
    }
    
    public void testGetRank(){
        int year = 1971;
        String name = "Frank";
        String gender ="M";
        int rank = getRank(year, name, gender);
        System.out.println("The name " + name + ", born in the year " + year + ", is ranked: " + rank);
    }

    public String getName(int year, int rank, String gender){
        FileResource fr = new FileResource("F:/Productivity/Java/us_babynames/us_babynames_by_year/yob"+year+".csv");
        int femaleCtr = 0, maleCtr = 0;        
        for(CSVRecord rec : fr.getCSVParser(false)){
            String currName = rec.get(0);
            if(rec.get(1).equals("F")){
                femaleCtr++;
                if((femaleCtr==rank) && (rec.get(1).equals(gender))){
                    return rec.get(0);
                }
            }
            else if(rec.get(1).equals("M")){
                maleCtr++;
                if((maleCtr==rank) && (rec.get(1).equals(gender))){
                    return rec.get(0);
                }
            }
        }
        return "NO NAME";
    }
    
    public void testGetName(){
        int year = 1982, rank = 450;
        String gender = "M";
        String name = getName(year,rank,gender);
        System.out.println("The rank " + rank + ", classified as the gender " + gender + ", is the name: " + name);
    }

    public void whatIsNameInYear (String name, int year, int newYear, String gender){
        int rank = getRank(year,name,gender);
        String newName = getName( newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " + newName + " if they were born in " + newYear);
    }
    
    public void testWhatIsNameInYear(){
        String name = "Susan", gender = "F";
        int year = 1972, newYear = 2014;
        whatIsNameInYear(name, year, newYear, gender);
    }
    
    public int yearOfHighestRank(String name, String gender){
        DirectoryResource dr = new DirectoryResource();
        int year = 1880;
        int highestRank = 0, tempRank = 0, noRankCtr = 0, fileCtr = 0, yearHighest = 0;
        for (File f : dr.selectedFiles()) {
           fileCtr++;
           tempRank = getRank(year, name, gender);
           if(tempRank == -1){
               noRankCtr ++;
           }
           else{
               if(tempRank<highestRank || highestRank == 0){
                   highestRank = tempRank;
                   yearHighest = year;
               }
           }
           year++;
           if(year==2015){
            break;
           }
        }
        //select multiple files
        //returns the year with the highest rank for the name and gender
        if (fileCtr==noRankCtr){
            return -1;
        }
        return yearHighest;
    }
    
    public void testYearOfHighestRank(){
        String name = "Genevieve";
        String gender = "F";
        int year = yearOfHighestRank(name, gender);
        System.out.println("The name " + name + " ranked highest in the year " + year);
    }
    
    public double getAverageRank (String name, String gender){
        DirectoryResource dr = new DirectoryResource();
        int year = 1880;
        int highestRank = 0, tempRank = 0, rankCtr = 0, totalRank = 0, noRankCtr = 0, fileCtr = 0;
        double aveRank = 0;
        for (File f : dr.selectedFiles()) {
           fileCtr++;
           tempRank = getRank(year, name, gender);
           //if name is not in the file, return -1
           //what if present in a file but not all the files
           //not present in the first file, but present in the 2nd file
           if(tempRank!=-1){
               totalRank +=tempRank;
               rankCtr++;
           }
           if(tempRank == -1){
               noRankCtr++;
           }
           year++;
           if(year==2015){
            break;
           }
        }
        if (fileCtr==noRankCtr){
            return -1;
        }
        else{
        aveRank = (double) totalRank/rankCtr;
        return aveRank;
        }
    }
    
    public void testGetAverageRank(){
        String name = "Robert";
        String gender = "M";
        double aveRank = getAverageRank(name, gender);
        System.out.println("The average rank of name " + name + " is: " + aveRank);
    }
    
    public int getTotalBirthsRankedHigher (int year, String name, String gender){
        FileResource fr = new FileResource();
        int rank = getRank(year,name,gender);
        int totalBirth = 0, rankCtr = 0;
        for(CSVRecord rec : fr.getCSVParser(false)){
            if(rec.get(1).equals(gender)){
                if(rank!=-1){
                    rankCtr++;
                    if(rankCtr == rank){
                        break;
                    }
                    int numBorn = Integer.parseInt(rec.get(2));
                    totalBirth+=numBorn;
                    System.out.println("numBorn " + rankCtr + ": " +numBorn);
                }
            }
        }
        return totalBirth;
    }
    
    public void testGetTotalBirthsRankedHigher(){
    int year = 1990;
    String name = "Drew", gender = "M";
    int totalBirth = getTotalBirthsRankedHigher(year,name,gender);
    System.out.println(totalBirth);
    }
    
}
