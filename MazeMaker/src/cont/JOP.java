package cont;

import javax.swing.JOptionPane;

//A class containing static methods used for creating JOP messages and input fields
public class JOP {

	//Creates a message box
	public static void msg(String msg, String title) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
	//Creates a message box that can accept input
	public static String in(String msg, String title){
		return JOptionPane.showInputDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
