

import java.math.BigInteger;
import java.util.Random;

public class HomomorphicFunctions 
{
	private BigInteger lambda,n,nsquare,g;
	
	public void GeneratingValues(BigInteger p, BigInteger q) 
	{
		n = p.multiply(q);
		nsquare = n.multiply(n);
		g = new BigInteger("2");
		lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).divide(
		p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));
	}
	
	public BigInteger Decryption(BigInteger c) 
	{
		BigInteger u = g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).modInverse(n);
		return c.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);
	}
	
	public BigInteger Encryption(BigInteger m) 
	{
		BigInteger r = new BigInteger(512, new Random());
		return g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare);
	}
	
	public BigInteger add(BigInteger m1,BigInteger m2)
    {
        BigInteger sum=m1.multiply(m2.mod(nsquare));
        return sum;
    }
	
	public BigInteger Subtract(BigInteger m1,BigInteger m2)
    {
        BigInteger m2in=m2.modPow(new BigInteger("-1"),nsquare);
        BigInteger sub=m1.multiply(m2in.mod(nsquare));
        return sub;
    }

}
