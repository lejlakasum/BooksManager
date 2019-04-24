package ba.unsa.etf.rma.lejla.projekatrma;


import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class KnjigePoznanikaResultReciever extends ResultReceiver {

    private Receiver mReceiver;

    public KnjigePoznanikaResultReciever(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver=receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resulData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(mReceiver!=null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}





















