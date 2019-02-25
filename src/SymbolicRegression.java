

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;

public class SymbolicRegression {
	private static JFileChooser file = new JFileChooser();
	private static final String fileName = "regression.txt";
	private static final int maxGen = 2000;
	private static ArrayList<Double> x = new ArrayList<Double>();
	private static ArrayList<Double> y = new ArrayList<Double>();

	public static void main(String[] args) throws InvalidConfigurationException {
		chooseDir();

		GPConfiguration con = new GPConfiguration();
		con.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
		con.setMaxInitDepth(10);
		con.setPopulationSize(100);
		con.setFitnessFunction(new GPMathProblem.FitnessFormula());
		con.setCrossoverProb(90f);
		GPMathProblem mp = new GPMathProblem(con, x, y);
		GPGenotype gp = mp.create();

		gp.setVerboseOutput(true);

		System.out.println(maxGen + " generations");
		for (int i = 0; i < maxGen; i++) {
			gp.evolve(1);
			if (gp.getAllTimeBest() != null && gp.getAllTimeBest().getFitnessValue() == 0) {
				System.out.println("\nProgram found  " + i + " generations\n");
				break;
			}
		}
		gp.outputSolution(gp.getAllTimeBest());
	}

	private static void chooseDir() {
		File train = null;

		// set up the file chooser
		file.setCurrentDirectory(new File("."));
		file.setDialogTitle("Select directory");
		

		// run the file chooser and check the user didn't hit cancel
		if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			// get the files in the selected directory and match them to
			// the files we need.
		train = file.getSelectedFile();
			


			// makes sure that no file is missing
			if (train == null) {
				JOptionPane.showMessageDialog(null, "No correct file found in the selected directory", "Error Found",
						JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			} else {
				readFile(train);
			}
		}
	}

	private static void readFile(File train) {

		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(train));
			try {
				line = br.readLine();
				line = br.readLine();
				while ((line = br.readLine()) != null) {
					Scanner s = new Scanner(line);
					x.add(s.nextDouble());
					y.add(s.nextDouble());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
