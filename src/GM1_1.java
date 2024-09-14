public class GM1_1 {

    private static double[] cumulativeArray(double[] originalArray) {
        double[] cumulativeArray = new double[originalArray.length];
        double sum = 0.0D;
        for (int i = 0; i < cumulativeArray.length; i++) {
            sum += originalArray[i];
            cumulativeArray[i] = sum;
        }
        return cumulativeArray;
    }

    private static double[] averageArray(double[] cumulativeArray) {
        double[] averageArray = new double[cumulativeArray.length - 1];
        for (int i = 0; i < averageArray.length; i++) {
            averageArray[i] = ((cumulativeArray[i] + cumulativeArray[(i + 1)]) / 2.0D);
        }
        return averageArray;
    }

    private static double[][] matrixB(double[] averageArray) {
        double[][] matrixB = new double[averageArray.length][2];
        for (int i = 0; i < matrixB.length; i++) {
            for (int j = 0; j < 2; j++) {
                if (j == 1)
                    matrixB[i][j] = 1.0D;
                else {
                    matrixB[i][j] = (-averageArray[i]);
                }
            }
        }
        return matrixB;
    }

    private static double[][] matrixBT(double[][] matrixB) {
        double[][] matrixBT = new double[2][matrixB.length];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < matrixB.length; j++) {
                matrixBT[i][j] = matrixB[j][i];
            }
        }
        return matrixBT;
    }

    private static double[][] matrixYn(double[] originalArray) {
        double[][] matrixYn = new double[originalArray.length - 1][1];
        for (int i = 0; i < matrixYn.length; i++) {
            for (int j = 0; j < 1; j++) {
                matrixYn[i][j] = originalArray[(i + 1)];
            }
        }
        return matrixYn;
    }

    private static double[][] matrixBxBT(double[][] matrixB, double[][] matrixBT) {
        double[][] matrixBxBT = new double[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < matrixB.length; k++) {
                    matrixBxBT[i][j] += matrixBT[i][k] * matrixB[k][j];
                }
            }
        }
        return matrixBxBT;
    }

    private static double[][] inverseMatrixBxBT(double[][] matrixBxBT) {
        if (matrixBxBT == null)
            return null;
        double[][] inverseMatrixBxBT = new double[2][2];
        inverseMatrixBxBT[0][0] = (1.0D / (matrixBxBT[0][0] * matrixBxBT[1][1] - matrixBxBT[0][1]
                * matrixBxBT[1][0]) * matrixBxBT[1][1]);
        inverseMatrixBxBT[0][1] = (1.0D / (matrixBxBT[0][0] * matrixBxBT[1][1] - matrixBxBT[0][1]
                * matrixBxBT[1][0]) * -matrixBxBT[0][1]);
        inverseMatrixBxBT[1][0] = (1.0D / (matrixBxBT[0][0] * matrixBxBT[1][1] - matrixBxBT[0][1]
                * matrixBxBT[1][0]) * -matrixBxBT[1][0]);
        inverseMatrixBxBT[1][1] = (1.0D / (matrixBxBT[0][0] * matrixBxBT[1][1] - matrixBxBT[0][1]
                * matrixBxBT[1][0]) * matrixBxBT[0][0]);
        return inverseMatrixBxBT;
    }

    private static double[][] inverseMatrixBxBT_xBT(
            double[][] inverseMatrixBxBT, double[][] matrixBT) {
        if ((inverseMatrixBxBT == null) || (matrixBT == null))
            return null;
        double[][] inverseMatrixBxBT_xBT = new double[2][matrixBT[0].length];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < matrixBT[0].length; j++) {
                for (int k = 0; k < 2; k++) {
                    inverseMatrixBxBT_xBT[i][j] += inverseMatrixBxBT[i][k]
                            * matrixBT[k][j];
                }
            }
        }
        return inverseMatrixBxBT_xBT;
    }

    private static double[][] inverseMatrixBxBT_xBTxYN(
            double[][] inverseMatrixBxBT_xBT, double[][] matrixYN) {
        if ((inverseMatrixBxBT_xBT == null) || (matrixYN == null))
            return null;
        double[][] inverseMatrixBxBT_xBTxYN = new double[2][1];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 1; j++) {
                for (int k = 0; k < matrixYN.length; k++) {
                    inverseMatrixBxBT_xBTxYN[i][j] += inverseMatrixBxBT_xBT[i][k]
                            * matrixYN[k][j];
                }
            }
        }
        return inverseMatrixBxBT_xBTxYN;
    }

    private static double[][] inverseMatrixBxBT_xBTxYN(double[] originalArray) {
        double[][] matrixYn = matrixYn(originalArray);
        double[] cumulativeArray = cumulativeArray(originalArray);
        double[] averageArray = averageArray(cumulativeArray);
        double[][] matrixB = matrixB(averageArray);
        double[][] matrixBT = matrixBT(matrixB);
        double[][] matrixBxBT = matrixBxBT(matrixB, matrixBT);
        double[][] inverseMatrixBxBT = inverseMatrixBxBT(matrixBxBT);
        double[][] inverseMatrixBxBT_xBT = inverseMatrixBxBT_xBT(
                inverseMatrixBxBT, matrixBT);
        double[][] inverseMatrixBxBT_xBTxYN = inverseMatrixBxBT_xBTxYN(
                inverseMatrixBxBT_xBT, matrixYn);
        return inverseMatrixBxBT_xBTxYN;
    }

    public static double getPredictiveValue(double[] originalArray, int num) {
        if ((originalArray == null) || (originalArray.length < 3))
            return 0.0D;
        double[][] inverseMatrixBxBT_xBTxYN = inverseMatrixBxBT_xBTxYN(originalArray);
        double a = inverseMatrixBxBT_xBTxYN[0][0];
        double u = inverseMatrixBxBT_xBTxYN[1][0];
        double au = 0.0D;
        if (a != 0.0D) {
            au = u / a;
        }
        return (originalArray[0] - au)
                * (Math.exp(-a * (num - 1)) - Math.exp(-a * (num - 2)));
    }

    public static void main(String[] args) {
//        double[] originalArray = { 9.66D, 5.14D, 4.65D, 6.58D };
        double[] originalArray = {71751,90065,106324.2,117601.3,130884,137853,154264};

        System.out.println(getPredictiveValue(originalArray,
                originalArray.length + 1));
    }

}
