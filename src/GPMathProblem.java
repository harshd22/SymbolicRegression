
import java.util.ArrayList;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.GPProblem;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;

public class GPMathProblem extends GPProblem {
	public static Variable v;
	protected static Float[] x = new Float[20];
	protected static Float[] y = new Float[20];
	private final GPConfiguration con;

	public GPMathProblem(GPConfiguration con, ArrayList<Double> input, ArrayList<Double> output)
			throws InvalidConfigurationException {
		this.con = con;

		for (int i = 0; i < 20; i++) {
			x[i] = input.get(i).floatValue();
			y[i] = output.get(i).floatValue();
			System.out.println(i + ") " + x[i] + " " + y[i]);
		}
	}

	@Override
	public GPGenotype create() throws InvalidConfigurationException {
		Class[] types = { CommandGene.FloatClass };
		Class[][] args = { {} };

		// Define the commands and terminals the GP is allowed to use.
		// -----------------------------------------------------------
		CommandGene[][] cg = {
				{ v = Variable.create(con, "X", CommandGene.FloatClass), new Add(con, CommandGene.FloatClass),
						new Subtract(con, CommandGene.FloatClass), new Multiply(con, CommandGene.FloatClass),
						new Divide(con, CommandGene.FloatClass), new Pow(con, CommandGene.FloatClass),
						new Terminal(con, CommandGene.FloatClass, 1.0d, 37.0d, false), } };
		// ----------------------------------------
		return GPGenotype.randomInitialGenotype(con, types, args, cg, 100, true);
	}

	public static class FitnessFormula extends GPFitnessFunction {

		@Override
		protected double evaluate(IGPProgram ind) {
			double foundError = 0.0f;
			Object[] placeHolder = new Object[0];
			for (int i = 0; i < 20; i++) {
				v.set(x[i]);
				try {
					double result = ind.execute_float(0, placeHolder);
					foundError += Math.abs(result - y[i]);
					if (Double.isInfinite(foundError)) {
						return Double.MAX_VALUE;
					}
				} catch (ArithmeticException ex) {
					// should not be a problem unless the wrong value was
					// entered
					System.out.println("x = " + x[i].floatValue());
					System.out.println(ind);
					throw ex;
				}
			}
			// if the error is small enough
			if (foundError < 0.001) {
				foundError = 0.0d;
			}
			return foundError;
		}

	}
}
