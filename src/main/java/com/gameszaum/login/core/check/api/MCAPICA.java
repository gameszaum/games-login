package com.gameszaum.login.core.check.api;

import com.gameszaum.login.core.check.Check;
import com.gameszaum.login.core.check.Verify;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * @author netindev
 */
public class MCAPICA implements Verify {

    private boolean result;

    @Override
    public boolean verify(String playerName) {
        try {
            BufferedImage image = ImageIO.read(new URL(Check.CheckAPI.MCAPI_CA.getLink() + playerName));
            try {
                if (image.getHeight() > 0) {
                    this.setResult(true);
                    return true;
                }
            } catch (Exception e) {
                this.setResult(true);
                return false;
            }
        } catch (Exception e) {
            this.setResult(false);
            return false;
        }
        return this.result;
    }

    private void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public boolean getResult() {
        return this.result;
    }

}