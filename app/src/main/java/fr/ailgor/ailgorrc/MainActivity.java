package fr.ailgor.ailgorrc;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ToggleButton;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;


public class MainActivity extends IOIOActivity {

    private ToggleButton toggleButtonLedSTAT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleButtonLedSTAT = (ToggleButton)findViewById(R.id.toggleButtonLedSTAT);

        enableUi(false);

    } // End onCreate

    private void enableUi(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                toggleButtonLedSTAT.setEnabled(enable); // the toggleButton is disable
                toggleButtonLedSTAT.setChecked(enable); // switch on the toggleButton
                if (enable)
                    Toast.makeText(MainActivity.this, getString(R.string.IOIOConnected), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, getString(R.string.IOIODisconnected), Toast.LENGTH_LONG).show();

            }
        });
    } // End enableUi


    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    } // End IOIOLooper


    class Looper extends BaseIOIOLooper{

// Declare PIN
        private DigitalOutput PinDO0; // LED STAT

       // Motor DC : Right
        private DigitalOutput PinDO6; // L293D
        private DigitalOutput PinDO7; // L293D
        private PwmOutput PinPW37; // L293D

        // Motor DC : Left
        private DigitalOutput PinDO11; // L293D
        private DigitalOutput PinDO12; // L293D
        private PwmOutput PinPW38; // L293D

        @Override
        protected void setup() throws ConnectionLostException, InterruptedException {
//            super.setup();
// Assign PIN
            // LED STAT
            PinDO0 = ioio_.openDigitalOutput(0, true);

           // Motor DC : Right
            PinDO6 = ioio_.openDigitalOutput(0);
            PinDO7 = ioio_.openDigitalOutput(0);
            PinPW38 = ioio_.openPwmOutput(38,100);


            // Motor DC : Left
            PinDO11 = ioio_.openDigitalOutput(0);
            PinDO12 = ioio_.openDigitalOutput(0);
            PinPW37 = ioio_.openPwmOutput(37,100);


            enableUi(true); // Start IOIO
        } // End setup

        @Override
        public void loop() throws ConnectionLostException, InterruptedException {
//            super.loop();


         PinDO0.write(!toggleButtonLedSTAT.isChecked());

         PinDO6.write(false);
         PinDO7.write(true);
         PinDO11.write(true);
         PinDO12.write(false);


            Thread.sleep(100);

        } // End Loop



        @Override
        public void disconnected() {
//            super.disconnected();
            enableUi(false);
            toggleButtonLedSTAT.setEnabled(false); // the toggleButton is disable
            toggleButtonLedSTAT.setChecked(false); // switch off the toggleButton
            Toast.makeText(MainActivity.this,getString(R.string.IOIODisconnected),Toast.LENGTH_LONG).show();

        } // End disconnected
    } //End Looper


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    } // End onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } // End onOptionsItemSelected
}
