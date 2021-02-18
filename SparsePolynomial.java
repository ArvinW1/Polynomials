import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class SparsePolynomial implements Polynomial{
    private String strPoly;
    private HashMap<Integer, Integer> intPoly;

    /**
     * Constructor of the class. Create a Sparsepolynomial object with the given string in the parameter and put it
     * in the private hashmap intPoly separating the exponent and coefficients of the polynomial.
     *
     * The exponent would be the key of the map and coefficient would be the value.
     *
     * Calls on wellformed to check the invariant of the string to see if the string is value. Throws an
     * illeqalargument exception when the class invariant doesn't hold true.
     *
     * @param str Takes in a polynomial written as a string
     * @throws IllegalArgumentException when the class invariant doesn't hold true.
     */
    public SparsePolynomial (String str){
        str = str.replaceAll("- ", "+-");
        str = str.replaceAll("\\s", "");
        strPoly = str;
        if(!wellFormed())
            throw new IllegalArgumentException();
        intPoly = new HashMap<Integer, Integer>();
        String[] newArrPoly = strPoly.split("\\+");
        int xPosition, carrotPosition, coef, exponent;
        for(String temp: newArrPoly){
            xPosition = temp.indexOf("x");
            if(xPosition != -1){
                carrotPosition = temp.indexOf("^");
                if(carrotPosition != -1)
                    exponent = Integer.parseInt(temp.substring(carrotPosition+1));
                else
                    exponent = 1;
                if(xPosition == 0)
                    coef = 1;
                else if(temp.substring(0,xPosition).equals("-"))
                    coef = -1;
                else
                    coef = Integer.parseInt(temp.substring(0, xPosition));
            }else{
                coef = Integer.parseInt(temp);
                exponent = 0;
            }
            intPoly.put(exponent, coef);
        }
    }

    /**
     * Private constructor of the class. Initializes the private hashmap with the given hashmap in the parameter to
     * construct a polynomial.
     *
     * @param hmap Takes in a map of polynomial with the exponent as the key and the coefficient as the value
     */
    private SparsePolynomial(HashMap<Integer, Integer> hmap){
        intPoly = hmap;
    }

    public HashMap<Integer, Integer> getIntPoly(){
        return intPoly;
    }

    /**
     * Takes the current instance and finds the largest exponent of the polynomial.
     * Returns the highest degree of the polynomial.
     *
     * @return the largest exponent with a non-zero coefficient.  If all terms have zero exponents, it returns 0.
     */
    @Override
    public int degree() {
        int greatest = Integer.MIN_VALUE;
        Set<Integer> key = intPoly.keySet();
        for(int i : key){
            if(greatest < i){
                greatest = i;
            }
        }
        return greatest;
    }

    /**
     * Takes the exponent given in the parameter and find the coefficient that corresponds with the exponent.
     * Returns the coefficient corresponding to the given exponent. Returns 0 if there is no term with that exponent
     * in the polynomial. In this case the hashmap would return null so 0 is return as a result.
     *
     * @param d the exponent whose coefficient is returned.
     * @return the coefficient of the term of whose exponent is d.
     */
    @Override
    public int getCoefficient(int d) {
        if(intPoly.get(d) == null)
            return 0;
        return intPoly.get(d);
    }

    /**
     * Takes the current instances and checks if the all the coefficients in the polynomial are all zeros. If they are
     * all zeros then return true otherwise false.
     *
     * @return true if the polynomial represents the zero constant otherwise false.
     */
    @Override
    public boolean isZero() {
        Set<Integer> key = intPoly.keySet();
        for(int i : key){
            if(intPoly.get(i) != 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Takes in a polynomial in the parameter and adds the coefficient of the two polynomial instance based on their
     * exponents. This is added into a new hashmap which is used to create a new polynomial instance.
     * This method is able to add a DensePolynomial with a SparsePolynomial and return a new SparsePolynomial
     * of the two instances added together in the end.
     *
     * Returns a polynomial by adding the parameter to the current instance. Neither the current instance nor the
     * parameter are modified.
     *
     * @param q the non-null polynomial to add to <code>this</code>
     * @return <code>this + </code>q as a SparsePolynomial object
     * @throws NullPointerException if q is null
     */
    @Override
    public Polynomial add(Polynomial q) {
        if (q == null)
            throw new NullPointerException();
        HashMap<Integer, Integer> newPoly = new HashMap<>();
        if(q instanceof SparsePolynomial){
            newPoly.putAll(intPoly);
            HashMap<Integer, Integer> tempPoly = ((SparsePolynomial) q).getIntPoly();
            Set<Integer> tempSet = tempPoly.keySet();
            for(int i: tempSet){
                if(newPoly.get(i) == null){
                    newPoly.put(i, q.getCoefficient(i));
                }else
                    newPoly.put(i, intPoly.get(i) + q.getCoefficient(i));
            }
        }else {
            newPoly.putAll(intPoly);
            int[] tempPoly = ((DensePolynomial) q).getIntPoly();
            int qSize = 0;
            for(int i: tempPoly){
                if(i != 0 && newPoly.get(qSize) == null){
                    newPoly.put(qSize, i);
                }else if(i != 0 && newPoly.get(qSize) != null){
                    newPoly.put(qSize, i+newPoly.get(qSize));
                }qSize++;
            }
        }
        return new SparsePolynomial(newPoly);
    }

    /**
     * Takes in a polynomial in the parameter and iterating through the map to get the coefficients of the two polynomial
     * and multiplies the coefficients then adds the exponent before inputting it into a new map. Then calling the add
     * method to combine any like terms.
     * This method is able to multiply a SparsePolynomial object with a DensePolynomial object and return the a new
     * SparsePolynomial object of the two instances multiplied together.
     *
     * Returns a polynomial by multiplying the parameter with the current instance.  Neither the current instance nor
     * the parameter are modified.
     *
     * @param q the polynomial to multiply with <code>this</code>
     * @return <code>this * </code>q
     * @throws NullPointerException if q is null
     */
    @Override
    public Polynomial multiply(Polynomial q) {
        if(q == null)
            throw new NullPointerException();
        HashMap<Integer, Integer> tempPoly = new HashMap<>();
        Polynomial newSparsePoly = new SparsePolynomial(tempPoly);
        if(q instanceof SparsePolynomial){ // Double For each loop
            Set<Integer> qExponent = ((SparsePolynomial) q).getIntPoly().keySet();
            Set<Integer> pExponent = intPoly.keySet();
            for(int i: pExponent){
                HashMap<Integer, Integer> temp = new HashMap<>();
                for(int j: qExponent){
                    int coef = intPoly.get(i) * q.getCoefficient(j);
                    int exponent = i + j;
                    temp.put(exponent, coef);
                }
                Polynomial tempSparse = new SparsePolynomial(temp);
                newSparsePoly = newSparsePoly.add(tempSparse);
            }
        }else{//Basically the same as the method in multiply from dense. similar but working withs maps
            int[] tempArr = ((DensePolynomial)q).getIntPoly();
            int qSize = tempArr.length-1;
            Set<Integer> pExponent = intPoly.keySet();
            for(int i: pExponent){
                HashMap<Integer, Integer> temp = new HashMap<>();
                for(int j = 0; j<= qSize; j++){
                    int exponent = i + j;
                    int coef = intPoly.get(i) * tempArr[j];
                    if(coef != 0)
                        temp.put(exponent, coef);
                }
                Polynomial tempSparse = new SparsePolynomial(temp);
                newSparsePoly = newSparsePoly.add(tempSparse);
            }
        }
        return newSparsePoly;
    }

    /**
     * Takes in a polynomial in the parameter and negates it using the minus method. Then calls the add method to add
     * the negated polynomial with the current instance. This method is able to take in a DensePolynomial and subtract
     * the polynomial with a Sparsepolynomial.
     *
     * Returns a  polynomial by subtracting the parameter from the current instance. Neither the current instance nor
     * the parameter are modified.
     *
     * @param q the non-null polynomial to subtract from <code>this</code>
     * @return <code>this - </code>q
     * @throws NullPointerException if q is null
     */
    @Override
    public Polynomial subtract(Polynomial q) {
        if(q == null)
            throw new NullPointerException();
        return this.add(q.minus());
    }

    /**
     *
     * Takes the current instance and negates the whole polynomial, creating a new polynomial with it.
     * Returns a polynomial by negating the current instance. The current instance is not modified.
     *
     * @return -this
     */
    @Override
    public Polynomial minus() {
        HashMap<Integer, Integer> newMap = new HashMap<>();
        Set<Integer> keyset = intPoly.keySet();
        for(int i: keyset){
            int coef = intPoly.get(i);
            newMap.put(i, coef*-1);
        }
        return new SparsePolynomial(newMap);
    }

    /**
     * Checks if the class invariant holds for the current instance. Checks the string of the polynomial that was passed
     * in the constructor parameter for is the exponents are in descending order and if there are any 0 or 1 in front of
     * the variable "x". Additionally, it checks for if there are any 0 coefficient added in the polynomial. If there are
     * 0 or 1 coefficient in front of the variable or 0 coefficients or exponent isn't in descending order then it would
     * return false. Additionally it checks for if there are any repeated exponents or any 0 or 1 in the exponent.
     * Otherwise true since the string held true to the class invariant.
     *
     * This is unable to verify fractions as integer even if the fraction does divide perfectly into an integer.
     * This numbers in the string must be integers and double or else it would false to hold the class invariant.
     *
     * @return {@literal true} if the class invariant holds, and {@literal false} otherwise.
     */
    @Override
    public boolean wellFormed() {
        if(strPoly.contains(".")) // If there is double
            return false;
        String[] newArrPoly = strPoly.split("\\+");
        String temp = newArrPoly[0];
        int indexofCarrot = 0;
        int StartingExponentValue = 0;
        if(temp.contains("x")){
            indexofCarrot = temp.indexOf("^");
            if(indexofCarrot == -1)
                StartingExponentValue = 1;
            else
                StartingExponentValue = Integer.parseInt(temp.substring(indexofCarrot+1)); // Gets the Exponent Value of the first character
        }
        int ExponentValue = 0;
        for(int i = 1; i < newArrPoly.length; i++){ // Going through the rest of the values to check if the exponents are in descending order
            temp = newArrPoly[i];
            if(temp.contains("x")){
                indexofCarrot = temp.indexOf("^");
                if(indexofCarrot == -1 )
                    ExponentValue = 1;
                else
                    ExponentValue = Integer.parseInt(temp.substring(indexofCarrot+1));
                if(ExponentValue == 0){
                    return false;
                }
            }else
                ExponentValue = 0;
            if(ExponentValue >= StartingExponentValue)
                return false;
            else
                StartingExponentValue = ExponentValue;
        }try{
            for(String str: newArrPoly){ //Checking the coefficient of the variables
                int indexOfX = str.indexOf("x");
                if(indexOfX != -1){
                    if(indexOfX != 0){
                        String tempString = str.substring(0, indexOfX);
                        if(tempString.equals("-"))
                            continue;
                        int coef = Integer.parseInt(tempString);
                        if(coef == 0 || coef == 1)
                            return false;
                    }
                }else{ // If it is just an coefficient
                    int coef = Integer.parseInt(str);
                    if(coef == 0 && str.length() != 1)
                        return false;
                }
            }
        }catch(Exception e) {
            return false;
        }
        return true;
    }

    /**
     * The current instance is written out as a string. Then returns the string representation of the polynomial.
     *
     * @return a string representation of the polynomial in the hashmap in descending order of the exponent
     */
    @Override
    public String toString() {
        String str = "";
        Set<Integer> tempSet = intPoly.keySet();
        Integer[] arr = new Integer[tempSet.size()];
        tempSet.toArray(arr);
        Arrays.sort(arr);
        int size = arr.length-1;
        for(int i = tempSet.size()-1; i >= 0; i--){
            if(intPoly.get(arr[i]) != 0){
                if(size == 0 && arr[i] == 0)
                    str = str + intPoly.get(arr[i]);
                else if(size == 0 && arr[i] != 0){
                    if(intPoly.get(arr[i]) != -1 && intPoly.get(arr[i]) != 1)
                        str = str + intPoly.get(arr[i]) + "x^" + arr[i];
                    else if(intPoly.get(arr[i]) == 1)
                        str = str + "x^" + arr[i];
                    else
                        str = str + "-x^" + arr[i];
                }else if(arr[i] == 0)
                    str = str + intPoly.get(arr[i]) + " + ";
                else if(arr[i] == 1 && intPoly.get(arr[i]) == 1)
                    str =str + "x" + " + ";
                else if(arr[i] == 1 && intPoly.get(arr[i]) == -1)
                    str =str + "-x" + " + ";
                else if(arr[i] == -1 && intPoly.get(arr[i]) == -1)
                    str =str + "-x^" + arr[i] + " + ";
                else if(arr[i] == -1 && intPoly.get(arr[i]) == 1)
                    str =str + "x^" + arr[i] + " + ";
                else if(intPoly.get(arr[i]) == -1)
                    str =str + "-x^" + arr[i] + " + ";
                else if(arr[i] == 1)
                    str = str + intPoly.get(arr[i]) + "x" + " + ";
                else if(intPoly.get(arr[i]) != 1)
                    str = str + intPoly.get(arr[i]) + "x^" + arr[i] + " + ";
                else if(intPoly.get(arr[i]) == 1)
                    str = str + "x^" + arr[i] + " + ";
            }
            size--;
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
     * This method checks if the object passed in the parameter is an instance of Sparsepolynomial and the content in
     * the parameter. If they are the same type and the contents are equal then true otherwise false.
     *
     * @param q Any object but more specifically a Densepolynomial or a Sparsepolynomial
     * @return True or false if the parameter is equal to the current instance
     */
    public boolean equals(Object q){
        if(q instanceof SparsePolynomial){
            HashMap<Integer, Integer> tempMap = ((SparsePolynomial) q).getIntPoly();
            HashMap<Integer, Integer> trackerMap = new HashMap<>();
            Set<Integer> qSet = tempMap.keySet();
            Set<Integer> pSet = intPoly.keySet();
            for(int i: pSet) {
                trackerMap.put(i,this.getCoefficient(i));
                if (qSet.contains(i) && this.getCoefficient(i) != ((SparsePolynomial) q).getCoefficient(i))
                    return false;
            }for(int j: qSet){
                if(!trackerMap.containsKey(j)){
                    if(((SparsePolynomial) q).getCoefficient(j) != 0)
                        return false;
                }
            }
            return true;
        }
        return false;
    }
}
