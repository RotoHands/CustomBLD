package cubes;

/**
 * Created by user on 05/09/2017.
 */

    public abstract interface BldCube { public abstract String getRotations();

        public static enum OrientationColor { WHITE(0),  ORANGE(1),  GREEN(2),  RED(3),  BLUE(4),  YELLOW(5);

            private final int internalNum;

            private OrientationColor(int internalNum) {
                this.internalNum = internalNum;
            }

            protected int getNum() {
                return internalNum;
            }
        }

        public abstract String getSolutionPairs(boolean paramBoolean, boolean iswWingSchemeRegular);

        public abstract String getStatstics();

        public abstract String getScramble();

        public abstract void parseScramble(String paramString);

        public abstract String getNoahtation();
        public static void test(){
            System.out.println("sdfsdfds");
        }
        

    }
   

    

