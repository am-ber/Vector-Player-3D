package com.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.metaget.Song;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JFileChooser fc;
	DefaultListModel<String> songs = new DefaultListModel<String>();
	private JList<String> songList = new JList<String>(songs);
	private File songDirectory = new File(".");
	JButton btnChangeLibrary;
	Song selectedSong;
	JLabel lblSongName;
	JLabel lblGenre;
	JLabel lblArtist;

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
		setTitle("Vector Player 3D");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVectorViewerd = new JLabel("Vector Player 3D");
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
		
		lblSongName = new JLabel("");
		lblSongName.setBounds(82, 11, 108, 14);
		panel.add(lblSongName);
		
		JLabel lblSongInfo2 = new JLabel("Genre:");
		lblSongInfo2.setBounds(10, 61, 73, 14);
		panel.add(lblSongInfo2);
		
		lblGenre = new JLabel("");
		lblGenre.setBounds(82, 61, 128, 14);
		panel.add(lblGenre);
		
		JLabel lblSongInfo3 = new JLabel("Artist:");
		lblSongInfo3.setBounds(10, 36, 37, 14);
		panel.add(lblSongInfo3);
		
		lblArtist = new JLabel("");
		lblArtist.setBounds(82, 36, 130, 14);
		panel.add(lblArtist);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setBounds(628, 11, 106, 45);
		contentPane.add(btnPlay);
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//pass to interpreter here
			}
		});
		
		final JButton btnAddSong = new JButton("Add");
		btnAddSong.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				File newSongDir = new File(getDirectory(false, btnAddSong, songDirectory));
				try {
					Files.copy(newSongDir.toPath(), new File(songDirectory.toString() + "\\" + newSongDir.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				refreshSongList(songDirectory);
			}
		});
		btnAddSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAddSong.setBounds(539, 11, 79, 45);
		contentPane.add(btnAddSong);
		
		btnChangeLibrary = new JButton("Change Library");
		btnChangeLibrary.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				songDirectory = new File(getDirectory(true, btnChangeLibrary, songDirectory));
				refreshSongList(songDirectory);
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
				refreshSongList(songDirectory);
			}
		});
		btnRefresh.setBounds(450, 11, 79, 45);
		contentPane.add(btnRefresh);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(220, 67, 514, 394);
		contentPane.add(scrollPane);
		

		songList.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				//refreshMetadata();
				
			}
		});
		songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		songList.setBackground(Color.LIGHT_GRAY);
		scrollPane.setViewportView(songList);
	}
	
	public void refreshMetadata(String testString) {
		if(songList.getSelectedValue() != null) {
		selectedSong = new Song(songDirectory + "\\" + songList.getSelectedValue());
		String name, artist, genre;
		name = selectedSong.getTitle();
		artist = selectedSong.getArtist();
		genre = selectedSong.getGenre();
		
		lblSongName.setText((name==null?"NO DATA FOUND":name));
		lblArtist.setText((artist==null?"NO DATA FOUND":artist));
		lblGenre.setText((genre==null?"NO DATA FOUND":genre));
		}
	}
	
	public int refreshSongList(File directory){
		int songCount = 0;
		songs.removeAllElements();
		File[] directoryListing = directory.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				String fName = child.getName();
				if(fName.toLowerCase().endsWith(".wav") || fName.toLowerCase().endsWith(".mp3")) {
					songs.addElement(fName);
					songCount++;
				}
			}
			return songCount;
		}
		return -1;
	}
	
	public String getDirectory(boolean directoriesOnly, JButton button, File defaultDirectory) {
		fc = new JFileChooser();
		fc.setCurrentDirectory(defaultDirectory);
		fc.setDialogTitle("Choose Song Folder");
		if(directoriesOnly)
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		if (fc.showOpenDialog(button) == JFileChooser.APPROVE_OPTION) { 
			return fc.getSelectedFile().getPath();
		}
		
		return songDirectory.getAbsolutePath();
	}
	
}
