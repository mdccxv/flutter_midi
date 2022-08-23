package com.appleeducate.fluttermidi;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import cn.sherlock.com.sun.media.sound.SF2Soundbank;
import cn.sherlock.com.sun.media.sound.SoftSynthesizer;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;
import jp.kshoji.javax.sound.midi.Receiver;
import jp.kshoji.javax.sound.midi.ShortMessage;

/** FlutterMidiPlugin */
public class FlutterMidiPlugin implements MethodCallHandler, FlutterPlugin {
  private Receiver recv;
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    channel = new MethodChannel(binding.getBinaryMessenger(), "flutter_midi");
    channel.setMethodCallHandler(this);
    Context applicationContext = binding.getApplicationContext();
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {

  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
    SoftSynthesizer synth;
    if (call.method.equals("prepare_midi")) {
      try {
        String _path = call.argument("path");
        File _file = new File(_path);
        SF2Soundbank sf = new SF2Soundbank(_file);
        synth = new SoftSynthesizer();
        synth.open();
        synth.loadAllInstruments(sf);
        synth.getChannels()[0].programChange(0);
        synth.getChannels()[1].programChange(1);
        recv = synth.getReceiver();
        result.success("Prepared Sound Font");
      } catch (IOException e) {
        e.printStackTrace();
        result.error("IOException", e.toString(), null);
      } catch (MidiUnavailableException e) {
        e.printStackTrace();
        result.error("MidiUnavailableException", e.toString(), null);
      }
    } else if (call.method.equals("change_sound")) {
      try {
        String _path = call.argument("path");
        File _file = new File(_path);
        SF2Soundbank sf = new SF2Soundbank(_file);
        synth = new SoftSynthesizer();
        synth.open();
        synth.loadAllInstruments(sf);
        synth.getChannels()[0].programChange(0);
        synth.getChannels()[1].programChange(1);
        recv = synth.getReceiver();
        result.success("Change sound");
      } catch (IOException e) {
        e.printStackTrace();
        result.error("IOException", e.toString(), null);
      } catch (MidiUnavailableException e) {
        e.printStackTrace();
        result.error("MidiUnavailableException", e.toString(), null);
      }
    } else if (call.method.equals("unmute")) {
      result.success("");
    } else if (call.method.equals("play_midi_note")) {
      int _note = call.argument("note");
      try {
        ShortMessage msg = new ShortMessage();
        msg.setMessage(ShortMessage.NOTE_ON, 0, _note, 127);
        recv.send(msg, -1);
        result.success("Playing");
      } catch (InvalidMidiDataException e) {
        e.printStackTrace();
        result.error("InvalidMidiDataException", e.toString(), null);
      }
    } else if (call.method.equals("stop_midi_note")) {
      int _note = call.argument("note");
      try {
        ShortMessage msg = new ShortMessage();
        msg.setMessage(ShortMessage.NOTE_OFF, 0, _note, 127);
        recv.send(msg, -1);
        result.success("Stopped");
      } catch (InvalidMidiDataException e) {
        e.printStackTrace();
        result.error("InvalidMidiDataException", e.toString(), null);
      }
    } else {
      result.notImplemented();
    }
  }
}
