import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class DensePolynomial implements Polynomial {
    private String strPoly;
    private int[] intPoly;

    /**
     * Constructor of the class. Creates a Densepolynomial object with the given polynomial as a string in the parameter
     * and puts it in the array, separating the string by the exponent and inputting the coefficient in the array.
     *
     * Calls on wellformed the check if the polynomial given the string is valid If it is not wellformed then
     * throws an illegalargument exception
     *
     * @param str Takes in polynomial which is written as a string
     * @throws IllegalArgumentException when the class invariant doesn't hold true
     */
    public DensePolynomial (String str){
        str = str.replaceAll("- ", "+-");
        str = str.replaceAll("\\s", "");
        strPoly = str;
        if(!wellFormed())
            throw new IllegalArgumentException();
        String[] newString = strPoly.split("\\+");
        int position;// Gets the greatest exponential value from the list
        for (String temp : newString) {
            if (temp.contains("x")) {
                if(temp.contains("^")){
                    int x = temp.indexOf("^");
                    position = Integer.parseInt(temp.substring(x+1));
                }else
                    position = 1;
                int indexofX = temp.indexOf("x");
                if(indexofX != 0) {
                    String tempString = temp.substring(0, indexofX);
                    int coef;
                    if(tempString.equals("-")){
                        coef = -1;
                    }else {
                        coef = Integer.parseInt(tempString);
                    }
                    intPoly[position] = coef;
                }else
                    intPoly[position] = 1;
            }else{
                int coef = Integer.parseInt(temp);
                intPoly[0] = coef;
            }
        }
    }

    /**
     * Private constructor of the class. Initializes the array with the array given in the parameter to construct a
     * polynomial.
     *
     * @param arr Takes in an int array of coefficient that represents a polynomial
     */
    private DensePolynomial (int [] arr){
        intPoly = arr;
    }

    public int[] getIntPoly(){
        return intPoly;
    }

    /**
     * Find the largest exponent of the current instance and returns the degree of the polynomial.
     *
     * @return the largest exponent with a non-zero coefficient.  If all terms have zero exponents, it returns 0.
     */
    @Override
    public int degree() {
        int i = 0;
        int exponent = 0;
        for(int x: intPoly){
            if(x != 0)
                exponent = i;
            i++;
        }
        return exponent;
    }

    /**
     * Finds the coefficient that corresponds with the given exponent in the parameter.
     * Returns the coefficient corresponding to the given exponent.  Returns 0 if there is no term with that exponent
     * in the polynomial.
     *
     * @param d the exponent whose coefficient is returned.
     * @return the coefficient of the term of whose exponent is d.
     */
    @Override
    public int getCoefficient(int d) {
        if(d > intPoly.length-1 || d < 0)
            return 0;
        return intPoly[d];
    }

    /**
     * Checks if the current instance is only contains zero. If it only contains 0 as the coefficient then it returns
     * true otherwise false.
     *
     * @return true if the polynomial represents the zero constant otherwise false.
     */
    @Override
    public boolean isZero() {
        for(int x: intPoly){
            if(x != 0)
                return false;
        }
        return true;
    }

    /**
     * Takes in a polynomial in the parameter and adds the coefficient based on if the exponents are the same.
     * This is added into a new int array to prevent any changes to the current instance nor the parameter.
     * This method is able to intake a SparsePolynomials however if the an exception is thrown when there is a negative
     * exponent an error is thrown.
     *
     * Returns a polynomial by adding the the parameter to the current instance, Neither the current instance nor the
     * parameter are modified.
     *
     * @param q the non-null polynomial to add to <code>this</code>
     * @return <code>this + </code>q
     * @throws NullPointerException if q is null
     * @throws IllegalArgumentException if there is an negative exponent
     */
    @Override // Add in a loop to check if there are any negatives in q
    public Polynomial add(Polynomial q) {
        if(q == null){
            throw new NullPointerException();
        }if(q instanceof SparsePolynomial){
            Set<Integer> tempSet = ((SparsePolynomial) q).getIntPoly().keySet();
            for(int i: tempSet){
                if(i < 0)
                    throw new IllegalArgumentException(); // If there is an negative exponent in sparse polynomial
            }
        }
        int pDegree = this.degree();
        int qDegree = q.degree();
        int[] newPoly;
        int greatest, difference;
        boolean thisGreater = false;
        if(pDegree > qDegree) {
            thisGreater = true;
            greatest = pDegree;
            newPoly = new int[pDegree+1];
        }
        else {
            greatest = qDegree;
            newPoly = new int[qDegree+1];
        }
            if(greatest == pDegree){
                difference = pDegree - qDegree;
            }else
                difference = qDegree - pDegree;
            for(int i =0; i <= (greatest-difference); i++){
                newPoly[i] = this.getCoefficient(i) + q.getCoefficient(i);
            }int newPosition = greatest - difference + 1;
            while(difference != 0){
                if(thisGreater)
                    newPoly[newPosition] = this.getCoefficient(newPosition);
                else
                    newPoly[newPosition] = q.getCoefficient(newPosition);
                difference --;
                newPosition++;
            }
        return new DensePolynomial(newPoly);
    }

    /**
     * Takes in a polynomial as a parameter and iterates through the coefficient in each of the instances and multiplies
     * them and adds the exponent to set the new position of the coefficient. Then it calls add to compile the multiplied
     * values into one simple polynomial.
     * This method is able to intake a SparsePolynomial object and multiply it with a Densepolynomial object. However
     * if an negative exponent occurs an IllegalArgumentException exception is thrown
     *
     * Returns a polynomial by multiplying the parameter with the current instance.  Neither the current instance nor
     * the parameter are modified.
     *
     * @param q the polynomial to multiply with <code>this</code>
     * @return <code>this * </code>q
     * @throws NullPointerException if q is null
     * @throws IllegalArgumentException if the exponent added together is negative
     */
    @Override
    public Polynomial multiply(Polynomial q) {
        if(q == null)
            throw new NullPointerException();
        int pDegree = this.degree();
        int qDegree = Math.abs(q.degree());
        int[] temp = new int[pDegree * qDegree + 1];
        Polynomial newDensePoly = new DensePolynomial(temp);
        if(q instanceof DensePolynomial) {
            for (int i = 0; i <= pDegree; i++) {
                temp = new int[i+qDegree+1];
                for(int j = 0; j <= qDegree; j++){
                    int coef = this.getCoefficient(i)*q.getCoefficient(j);
                    int position = i+j;
                    temp[position] = coef;
                }
                Polynomial tempDense = new DensePolynomial(temp);
                newDensePoly = newDensePoly.add(tempDense);
            }
        }else{
            HashMap<Integer, Integer> SparsePoly = ((SparsePolynomial) q).getIntPoly();
            Set<Integer> tempSet = SparsePoly.keySet();
            for(int i =0; i <= pDegree; i++){
                temp = new int[i + Math.abs(q.degree()) +1];
                for(int j: tempSet){
                    int position = i+j;
                    int coef = this.getCoefficient(i)*q.getCoefficient(j);
                    if(position >= 0) {
                    temp[position] = coef;
                    }else if(position < 0 && coef == 0)
                        continue;
                    else{
                        throw new IllegalArgumentException();
                    }
                }
                Polynomial tempDense = new DensePolynomial(temp);
                newDensePoly = newDensePoly.add(tempDense);
            }
        }
        return newDensePoly;
    }

    /**
     * Takes in a polynomial in the parameter and negates it using the minus method. Then adds it to the other current
     * instance. This method is able to intake a sparsePolynomial and subtract it with a densepolynomial
     * but if the polynomial contains a negative exponent then a illegalargument exception is going to be thrown.
     * Returns a polynomial by subtracting the parameter from the current instance. Neither the current instance nor
     * the parameter are modified.
     *
     * @param q the non-null polynomial to subtract from <code>this</code>
     * @return <code>this - </code>q
     * @throws NullPointerException if q is null
     * @throws IllegalArgumentException if the polynomial subtracted together contains a negative exponent
     */
    @Override
    public Polynomial subtract(Polynomial q) {
        if(q == null)
            throw new NullPointerException();
        if(q instanceof SparsePolynomial){
            Set<Integer> tempSet = ((SparsePolynomial) q).getIntPoly().keySet();
            for(int i: tempSet){
                if(i < 0)
                    throw new IllegalArgumentException(); // If there is an negative exponent in sparse polynomial
            }
        }
        return this.add(q.minus());
    }

    /**
     * Takes the current instance and negates the whole polynomial, creating a new polynomial with it
     * Returns a polynomial by negating the current instance. The current instance is not modified.
     *
     * @return -this
     */
    @Override
    public Polynomial minus() {
        int[] newPoly = new int[intPoly.length];
        int i = 0;
        for(int x : intPoly){
            newPoly[i] = x * -1;
            i++;
        }
        return new DensePolynomial(newPoly);
    }

    /**
     * Checks if the class invariant holds for the current instance. Checks the string of polynomial that was passed
     * in the constructor parameter for if the exponents are in descending order and if there are any 0 or 1 in front
     * of the variable "x". Additionally, it checks for is there is a 0 coefficient added in the polynomial.
     * The numbers in the string must be integers and double or else it would false to hold the class invariant.
     * If there are 0 and 1 coefficient in front of the variable or 0 coefficients or exponent isn't in descending order
     * then it would return false. Additionally, checks if there are any 1 or 0 in the exponent or negative exponents.
     * Otherwise true.
     *
     * This is unable to verify fractions as integer even if the fraction does divide perfectly into an integer.
     *
     * It would initialize the size of the array once finding that the invariant holds true
     *
     * @return {@literal true} if the class invariant holds, and {@literal false} otherwise.
     */
    @Override
    public boolean wellFormed() {
        if(strPoly.contains("."))
            return false;
        String[] newString = strPoly.split("\\+");
        String temp = newString[0];
        int tracker = 0;
        int StartingExponent = 0;
        if (temp.contains("x")) {
            tracker = temp.indexOf("^");
            if (tracker == -1) // Checks if it is just an coefficient
                StartingExponent = 1;
            else {
                StartingExponent = Integer.parseInt(temp.substring(tracker + 1)); // Gets starting exponent value
                if (StartingExponent < newString.length-1) // Checks if the amount of elements is less than the exponent value
                    return false;
            }
        }else{// If the starting value is an coefficient and there is only one value in the array
            if(newString.length > 1)
                return false;
        }
        intPoly = new int[StartingExponent+1];
        int tempTracker = 1; // Goes through the index
        int ExponentValue = 0;
        while(tempTracker < newString.length){ // Loops through the whole array of values and checks if it is in decreasing order
            temp = newString[tempTracker];
            if(temp.contains("x")) {
                tracker = temp.indexOf("^");
                if (tracker == -1)
                    ExponentValue = 1;
                else
                    ExponentValue = Integer.parseInt(temp.substring(tracker+1));
                if(ExponentValue == 0) // If there is an coefficent with exponent of 0
                    return false;
            } else
                ExponentValue = 0;
            if(ExponentValue < 0) // Negative Exponents
                return false;
            if(ExponentValue >= StartingExponent)
                return false;
            else
                StartingExponent = ExponentValue;
            tempTracker++;
        }try {
            for (String s : newString) {
                if (s.contains("x")) {
                    int indexofX = s.indexOf("x");
                    if(indexofX != 0) {
                        String tempString = s.substring(0, indexofX);
                        if(tempString.equals("-"))
                            continue;
                        int coef = Integer.parseInt(tempString);
                        if (coef == 0 || coef == 1)
                            return false;
                    }
                }else{
                    int coef = Integer.parseInt(s);
                    if(coef == 0 && intPoly.length != 1)
                        return false;
                }
            }
        }catch(Exception e){
            return false;
        }
        return true;
    }

    /**
     * The current instance is written out as a string. Then returns the string representation of the polynomial.
     *
     * @return the String representation of the polynomial in descending order
     */
    @Override
    public String toString() {
        String str = "";
        int polySize = intPoly.length - 1;
        for(int i = polySize; i >= 0; i--){
            if(i == 0 && intPoly[i] != 0)
                str = str + intPoly[i];
            else if(i == 1 && intPoly[i] == 1)
                str = str + "x" + " + ";
            else if(i == 1 && intPoly[i] == -1)
                str = str + "-x" + " + ";
            else if(intPoly[i] == -1)
                str = str  +"-x^" + i + " + ";
            else if(i == 1 && intPoly[i] != 0)
                str = str + intPoly[i] +"x" + " + ";
            else if(intPoly[i] != 0 && intPoly[i] != 1)
                str = str + intPoly[i] +"x^" + i + " + ";
            else if(intPoly[i] == 1)
                str = str + "x^" + i + " + ";
//            if(i > 1 && intPoly[i] != 0)
//                str = str + " + ";
        }if(str.isEmpty()){
            str = "0";
        }if(str.length() > 3) {
            String temp = str.substring(str.length() - 2);
            String tempFront = str.substring(0, 2);
            if (temp.contains("+"))
                str = str.substring(0, str.length() - 3);
            else if(tempFront.contains("+"))
                str = str.substring(3);
        }
        return str;
    }

    /**
     * This method checks if the two object the current instance and the object passed in the parameter are equal to
     * each other by checking the type and if the contents are equal. If they are not false, otherwise true.
     *
     * @param q A polynomial object that is either DensePolynomial or SparsePolynomial or any object
     * @return True or false if the parameter object is equal to the current instance
     */
    public boolean equals(Object q){
        if(q instanceof DensePolynomial){
            boolean first = false;
            int greatest = this.degree();
            if(greatest > ((DensePolynomial) q).degree()) {
                first = true;
                greatest = ((DensePolynomial) q).degree();
            }
            for(int i= 0; i < greatest; i++){
                if(this.getCoefficient(i) != ((DensePolynomial) q).getCoefficient(i))
                    return false;
            }if(first){
                for(int i = greatest; i < this.degree(); i++){
                    if(this.getCoefficient(i) != 0)
                        return false;
                }
            }else {
                for (int i = greatest; i < ((DensePolynomial) q).degree(); i++) {
                    if (((DensePolynomial) q).getCoefficient(i) != 0)
                        return false;
                }
            }
            return true;
        }
        return false;
    }
}
