import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;

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
		frmAlphaconverter.setBounds(100, 100, 278, 124);
		frmAlphaconverter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frmAlphaconverter.getContentPane().setLayout(null);
		
		JPanel panel = new DragPanel(){
			@Override
			public void performFileAction(File file) {
				performPremultiplication(file);
				super.performFileAction(file);
			}
		};
		panel.setBackground(Color.WHITE);
		panel.setBounds(12, 12, 254, 100);
		frmAlphaconverter.getContentPane().add(panel);
		
		JLabel lblDragImageHere = new JLabel("Drag Image here");
		panel.add(lblDragImageHere);
	}

	private void performPremultiplication(File image) {
		if (image == null) {
			JOptionPane.showMessageDialog(null, "No Image selected.");
			return;
		}

		BufferedImage buffer = readImage(image);

		if (buffer == null) {
			JOptionPane.showMessageDialog(null, "Error getting image from " + image.getName());
			return;
		}

		if (isPremultiplied(buffer)) {
			JOptionPane.showMessageDialog(null, "Image " + image.getName() + " already premultiplied.");
			return;
		}

		buffer = doPremultiply(buffer);

		writeImage(buffer, image);
	}

	private File getImageFromFileChooser() {
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
