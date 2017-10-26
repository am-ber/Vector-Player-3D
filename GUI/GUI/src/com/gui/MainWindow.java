package com.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.metaget.MetaData;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JFileChooser fc;
	DefaultListModel<String> songs = new DefaultListModel<String>();
	private JList<String> songList = new JList<String>(songs);
	private File songDirectory;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setResizable(false);
		setTitle("Vector Viewer 3D");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVectorViewerd = new JLabel("Vector Viewer 3D");
		lblVectorViewerd.setFont(new Font("Tahoma", Font.BOLD, 32));
		lblVectorViewerd.setBounds(10, 11, 291, 45);
		contentPane.add(lblVectorViewerd);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(10, 67, 200, 394);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblSongInfo = new JLabel("Song Name:");
		lblSongInfo.setBounds(10, 11, 84, 14);
		panel.add(lblSongInfo);
		
		final JLabel lblSongName = new JLabel("");
		lblSongName.setBounds(82, 11, 108, 14);
		panel.add(lblSongName);
		
		JLabel lblSongInfo2 = new JLabel("Album Art:");
		lblSongInfo2.setBounds(10, 61, 73, 14);
		panel.add(lblSongInfo2);
		
		JLabel lblAlbumArt = new JLabel("");
		lblAlbumArt.setBounds(10, 86, 180, 180);
		panel.add(lblAlbumArt);
		
		JLabel lblSongInfo3 = new JLabel("Artist:");
		lblSongInfo3.setBounds(10, 36, 37, 14);
		panel.add(lblSongInfo3);
		
		final JLabel lblArtist = new JLabel("");
		lblArtist.setBounds(60, 36, 130, 14);
		panel.add(lblArtist);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setBounds(628, 11, 106, 45);
		contentPane.add(btnPlay);
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//refreshMetadata();
				File song = new File(songDirectory + songList.getSelectedValue());
				
				@SuppressWarnings("unused")
				MetaData meta = new MetaData(song);
				
				lblSongName.setText("Song name");
				lblArtist.setText("Artist name");
				//remove 'final's when issue is resolved
			}
		});
		
		final JButton btnAddSong = new JButton("Add");
		btnAddSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAddSong.setBounds(539, 11, 79, 45);
		contentPane.add(btnAddSong);
		
		JButton btnChangeLibrary = new JButton("Change Library");
		btnChangeLibrary.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				fc = new JFileChooser();
				fc.setCurrentDirectory(new java.io.File("."));
				fc.setDialogTitle("Choose Song Folder");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setAcceptAllFileFilterUsed(false);
				
				if (fc.showOpenDialog(btnAddSong) == JFileChooser.APPROVE_OPTION) { 
					songDirectory = fc.getSelectedFile();
					refreshSongList();
				}
			}
		});
		btnChangeLibrary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnChangeLibrary.setBounds(311, 11, 129, 45);
		contentPane.add(btnChangeLibrary);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refreshSongList();
			}
		});
		btnRefresh.setBounds(450, 11, 79, 45);
		contentPane.add(btnRefresh);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(220, 67, 514, 394);
		contentPane.add(scrollPane);
		
		songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		songList.setBackground(Color.LIGHT_GRAY);
		scrollPane.setViewportView(songList);
	}
	
	public void refreshMetadata() {
		//move code here when done
	}
	
	public void refreshSongList(){
		songs.removeAllElements();
		File[] directoryListing = songDirectory.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				String fName = child.getName();
				if(fName.toLowerCase().endsWith(".wav") || fName.toLowerCase().endsWith(".mp3"))
				songs.addElement(fName);
			}
		}
	}
}
