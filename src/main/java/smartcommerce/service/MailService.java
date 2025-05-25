package smartcommerce.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {
	
	@Autowired
	private final JavaMailSender mailSender;
	
	@Value("${app.dev-mode}")
	private boolean devMode;

    public void sendOrderConfirmationHtml(String toEmail, String name, String orderId, String summaryHtml) {
    	
    	try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(devMode ? "kharealiu@gmail.com" : toEmail);  // flip this in application.properties file
            helper.setSubject("🛍️ Order Confirmation – Smart Commerce");

            String htmlContent = """
            		  <html>
            		    <body style="font-family: Arial, sans-serif; background: #f5f7fa; padding: 20px; margin: 0;">
            		      <table width="100%%" cellpadding="0" cellspacing="0" style="max-width: 600px; margin: auto; background: #ffffff; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.05);">
            		        <tr>
            		          <td style="background: #007bff; padding: 20px 30px; color: white; border-top-left-radius: 8px; border-top-right-radius: 8px;">
            		            <h2 style="margin: 0;">Smart Commerce</h2>
            		            <p style="margin: 5px 0 0;">Order Confirmation</p>
            		          </td>
            		        </tr>
            		        <tr>
            		          <td style="padding: 30px;">
            		            <p>Hi <strong>%s</strong>,</p>
            		            <p>Thanks for shopping with us! Your order <strong>#%s</strong> has been successfully placed.</p>

            		            <h4 style="margin-top: 30px;">Your Order:</h4>
            		            <div style="padding: 10px 20px; background: #f1f3f6; border-radius: 6px; margin-bottom: 20px;">
            		              %s
            		            </div>

            		            <p>We’ll send you another email when your order ships. You can track your order anytime from your <a href="https://yourdomain.com/account/orders" style="color: #007bff;">account dashboard</a>.</p>

            		            <p style="margin-top: 40px;">Need help? <a href="mailto:support@smartcommerce.com">Contact our support team</a>.</p>
            		            <p style="font-size: 12px; color: #888;">This is an automated message from Smart Commerce. Do not reply to this email.</p>
            		          </td>
            		        </tr>
            		        <tr>
            		          <td style="background: #f9f9f9; text-align: center; font-size: 12px; color: #999; padding: 15px; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;">
            		            &copy; 2025 Smart Commerce, All rights reserved.
            		          </td>
            		        </tr>
            		      </table>
            		    </body>
            		  </html>
            		""".formatted(name, orderId, summaryHtml);


            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

