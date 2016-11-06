import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

public class AlphaConverter {

	private JFrame frmAlphaconverter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AlphaConverter window = new AlphaConverter();
					window.frmAlphaconverter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AlphaConverter() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAlphaconverter = new JFrame();
		frmAlphaconverter.setTitle("AlphaConverter");
		frmAlphaconverter.setBounds(100, 100, 240, 122);
		frmAlphaconverter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAlphaconverter.getContentPane().setLayout(null);

		JButton btnNewButton = new JButton("Load Image");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performPremultiply();
			}
		});
		btnNewButton.setBounds(63, 55, 117, 25);
		frmAlphaconverter.getContentPane().add(btnNewButton);
	}

	private void performPremultiply() {
		File image = getImage();

		if (image == null) {
			JOptionPane.showMessageDialog(null, "No Image selected.");
			return;
		}

		BufferedImage buffer = readImage(image);

		if (buffer == null) {
			JOptionPane.showMessageDialog(null, "Error getting image.");
			return;
		}

		if (isPremultiplied(buffer)) {
			JOptionPane.showMessageDialog(null, "Image already premultiplied.");
			return;
		}

		buffer = doPremultiply(buffer);

		writeImage(buffer, image);
	}

	private File getImage() {
		File selected_file;
		JFileChooser file_chooser = new JFileChooser();
		int file_chooser_result = 0;
		file_chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		file_chooser_result = file_chooser.showOpenDialog(frmAlphaconverter);
		if (file_chooser_result == JFileChooser.APPROVE_OPTION) {
			selected_file = file_chooser.getSelectedFile();
			return selected_file;
		}
		return null;
	}

	private BufferedImage readImage(File image) {
		try {
			BufferedImage buffer = ImageIO.read(image);
			return buffer;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error reading image.");
			e.printStackTrace();
			return null;
		}
	}

	private void writeImage(BufferedImage buffer, File image) {
		try {
			ImageIO.write(buffer, "png", image);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error writing image.");
			e.printStackTrace();
		}
	}

	private boolean isPremultiplied(BufferedImage buffer) {
		int width = buffer.getWidth();
		int height = buffer.getHeight();
		WritableRaster raster = buffer.getRaster();

		for (int xx = 0; xx < width; xx++) {
			for (int yy = 0; yy < height; yy++) {
				int[] pixels = raster.getPixel(xx, yy, (int[]) null);
				if (pixels[3] != 255) {
					if (pixels[0] > pixels[3] || pixels[1] > pixels[3] || pixels[2] > pixels[3]) {
						return false;
					}
				}
			}
		}

		return true;
	}

	private BufferedImage doPremultiply(BufferedImage buffer) {
		int width = buffer.getWidth();
		int height = buffer.getHeight();
		WritableRaster raster = buffer.getRaster();

		for (int xx = 0; xx < width; xx++) {
			for (int yy = 0; yy < height; yy++) {
				int[] pixels = raster.getPixel(xx, yy, (int[]) null);
				pixels[0] = (int) (pixels[0] * (pixels[3] / 255f));
				pixels[1] = (int) (pixels[1] * (pixels[3] / 255f));
				pixels[2] = (int) (pixels[2] * (pixels[3] / 255f));
				raster.setPixel(xx, yy, pixels);
			}
		}

		return buffer;
	}
}
