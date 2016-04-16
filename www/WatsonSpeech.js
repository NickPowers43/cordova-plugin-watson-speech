/*global cordova, module*/

window.watson = {
  stt: {
    init: function (credentials, messageCallback, errorCallback) {
      cordova.exec(messageCallback, errorCallback, "WatsonSpeechPlugin", "stt_init", [credentials]);
    },
    start: function () {
      cordova.exec(null, null, "WatsonSpeechPlugin", "stt_start", []);
    },
    stop: function () {
      cordova.exec(null, null, "WatsonSpeechPlugin", "stt_stop", []);
    }
  },
  tts: {
    init: function (credentials, voice, messageCallback, errorCallback) {
      cordova.exec(messageCallback, errorCallback, "WatsonSpeechPlugin", "tts_init", [credentials, voice]);
    },
    synthesize: function (text) {
      cordova.exec(null, null, "WatsonSpeechPlugin", "tts_synthesize", [text]);
    }
  }
};

/*window.stt_init = function (credentials, messageCallback, errorCallback) {
    console.log("stt_init");
    cordova.exec(messageCallback, errorCallback, "WatsonSpeechPlugin", "stt_init", [credentials]);
};
window.stt_start = function () {
    console.log("stt_start");
    cordova.exec(null, null, "WatsonSpeechPlugin", "stt_start", []);
};
window.stt_stop = function () {
    console.log("stt_stop");
    cordova.exec(null, null, "WatsonSpeechPlugin", "stt_stop", []);
};
window.tts_init = function (credentials, voice, messageCallback, errorCallback) {
    console.log("tts_init");
    cordova.exec(messageCallback, errorCallback, "WatsonSpeechPlugin", "tts_init", [credentials, voice]);
};
window.tts_synthesize = function (text) {
    console.log("tts_synthesize");
    cordova.exec(null, null, "WatsonSpeechPlugin", "tts_synthesize", [text]);
};*/
