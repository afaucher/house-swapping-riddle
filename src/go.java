
public class go {
    
    public enum color {
        green,
        red,
        yellow,
        blue,
        white
    }
    
    public enum nationality {
        brit,
        swede,
        dane,
        norwegian,
        german
    }
    
    public enum beverage {
        tea,
        coffee,
        milk,
        beer,
        water
    }
    
    public enum cigar {
        pallmall,
        dunhill,
        blend,
        bluemaster,
        prince
    }
    
    public enum pet {
        fish,
        dog,
        bird,
        cats,
        horse
    }
    
    public static void print(String loc, combo[] h) {
        System.out.println("============ " + loc + " ===========");
        for (int i = 0; i < 5; i++) {
            System.out.println("h:" + i + " " + h[i].b + "," + h[i].co + "," +  h[i].n + "," + h[i].ci + "," + h[i].p);
        }
    }
    
    public static Enum[] getOptions(Enum[][] o, int level) {
        return o[level];
    }
    
    public static void set(combo c, int level, Enum v) {
        switch (level) {
            case 0:
                c.b = (beverage)v;
                return;
            case 1:
                c.co = (color)v;
                return;
            case 2:
                c.n = (nationality)v;
                return;
            case 3:
                c.ci = (cigar)v;
                return;
            case 4:
                c.p = (pet)v;
                return;
        }
    }
    
    public static Enum get(combo c, int level) {
        switch (level) {
            case 0:
                return c.b;
            case 1:
                return c.co;
            case 2:
                return c.n;
            case 3:
                return c.ci;
            case 4:
                return c.p;
            default:
                throw new RuntimeException("?");
        }
    }
    
    public class combo {
       color co;
       nationality n;
       beverage b;
       cigar ci;
       pet p; 
    }
    
    public static boolean test(combo[] houses) {
        int white_index = -1;
        int green_index = -1;
        int blend_index = -1;
        int cat_index = -1;
        int horse_index = -1;
        int dunhill_index = -1;
        int norwegian_index = -1;
        int blue_index = -1;
        int water_index = -1;
        
        for (int i = 0; i < 5; i++) {
            //1
            if (houses[i].n == nationality.brit && houses[i].co != null && houses[i].co != color.red) return false;
            //2
            if (houses[i].n == nationality.swede && houses[i].p != null && houses[i].p != pet.dog) return false;
            //3
            if (houses[i].n == nationality.dane && houses[i].b != null && houses[i].b != beverage.tea) return false;
            //5
            if (houses[i].ci == cigar.pallmall && houses[i].p != null && houses[i].p != pet.bird) return false;
            //6
            if (houses[i].co == color.yellow && houses[i].ci != null && houses[i].ci != cigar.dunhill) return false;
            //7
            if (houses[i].b == beverage.milk && i != 2) return false;
            //8
            if (houses[i].n == nationality.norwegian && i != 0) return false;
            //11
            if (houses[i].ci == cigar.bluemaster && houses[i].b != null && houses[i].b != beverage.beer) return false;
            //12
            if (houses[i].n == nationality.german && houses[i].ci != null && houses[i].ci != cigar.prince) return false;
            
            if (houses[i].co == color.green) green_index = i;
            if (houses[i].co == color.white) white_index = i;
            if (houses[i].ci == cigar.blend) blend_index = i;
            if (houses[i].ci == cigar.dunhill) dunhill_index = i;
            if (houses[i].p == pet.cats) cat_index = i;
            if (houses[i].p == pet.horse) horse_index = i;
            if (houses[i].n == nationality.norwegian) norwegian_index = i;
            if (houses[i].co == color.blue) blue_index = i;
            if (houses[i].b == beverage.water) water_index = i;
        }
        
        //4
        if (green_index != -1 && white_index != -1 && green_index > white_index) return false;
        //9
        if (blend_index != -1 && cat_index != -1 && Math.abs(blend_index - cat_index) != 1) return false;
        //10
        if (horse_index != -1 && dunhill_index != -1 && Math.abs(horse_index - dunhill_index) != 1) return false;
        //13
        if (norwegian_index != -1 && blue_index != -1 && Math.abs(norwegian_index - blue_index) != 1) return false;
        //14
        if (blend_index != -1 && water_index != -1 && Math.abs(blend_index - water_index) != 1) return false;
        
        return true;
    }
    
    public static boolean tryAll(combo[] h, Enum[][] o, int level) {
        System.out.println("Level: " + level);
        int next_index = -1;
        boolean found[] = new boolean[5];
        for (int i = 0; i < 5; i++) {
            found[i] = false;
        }
       //Check for existing allocation
        for (int i = 0; i < 5; i++) {
            Enum e = get(h[i],level); 
            if (e == null) {
                next_index = i;
                break;
            }
            found[e.ordinal()] = true;
        }
        if (next_index == -1) {
            //No violations and we finished, try next preference
            if (level == 4) {
                //All preferences tried, check final solution
                print("Testing End",h);
                boolean success = test(h);
                if (success) {
                    print("Solution",h);
                } else {
                    System.out.println("Invalid solution");
                }
                return success;
            }
            print("Next Level",h);
            return tryAll(h,o,level+1);
        }
        System.out.println("Next Index: " + next_index);
        for (Enum b : getOptions(o,level)) {
            if (found[b.ordinal()]) {
                //Already allocated
                continue;
            }
            set(h[next_index],level,b);
            print("Testing",h);
            if (!test(h)) {
                System.out.println("Invalid solution");
                continue;
            }
            
            if (tryAll(h,o, level)) {
                //Done
                return true;
            }
        }
        //Backtrack
        set(h[next_index],level,null);
        return false;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        go g = new go();
        combo[] h = new combo[5];
        for (int i = 0; i < 5; i++) {
            h[i] = g.new combo();
        }
        Enum[][] o = new Enum[5][5];
        o[0] = beverage.values();
        o[1] = color.values();
        o[2] = nationality.values();
        o[3] = cigar.values();
        o[4] = pet.values();
        
        tryAll(h,o,0);
    }

}
