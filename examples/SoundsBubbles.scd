/*
SOUND BUBBLES
Sound Implementation of Frozen Bubbles OSCfied

ver 0.34
07.07.08
Juan Gabriel Alzate
http://www.rukano.de
*/

(
q = ();
q.bub = ();
q.freq = 40;
q.factor = 50;

// unixCmd("open " ++ Document.current.path.dirname ++ "/../FrozenBubbleOSCified.jar");
//---SYNTHS BUBBLES

SynthDef(\bubBlack, { |out, freq=2000, rq=0.5, dur=1, amp=0.01, gate=1, pan=0, decay=0.1|
	var snd;
	snd = SinOsc.ar(SinOsc.kr(freq/3, 0, freq/10, freq + LFNoise2.kr(Rand(2, 10), freq/10)), 0, SinOsc.kr(freq/8));
	snd = snd * EnvGen.kr(Env.adsr(0.1, 3, 0.1, 1, 0.3 ), gate, doneAction:2);
	snd = Pan2.ar(snd, pan, amp * 3 * LFNoise2.kr(Rand(2, 10), 0.2, 0.8) );
	Out.ar(out, snd);
}).store;

SynthDef(\bubSilver, { |out, freq=440, rq=0.5, dur=1, amp=0.01, gate=1, pan=0, decay=0.1|
	var snd;
	snd = Pulse.ar(SinOsc.kr(45, 0, freq/10, freq + LFNoise2.kr(Rand(2, 10), freq/10)), SinOsc.kr(4), SinOsc.kr(8));
	snd = snd * EnvGen.kr(Env.adsr(0.1, 3, 0.1, 1, 0.3 ), gate, doneAction:2);
	snd = Pan2.ar(snd, pan, amp * 2.5 * LFNoise2.kr(Rand(2, 10), 0.2, 0.8) );
	Out.ar(out, snd);
}).store;

SynthDef(\bubBlue, { |out, freq=440, rq=0.005, dur=1, amp=0.01, gate=1, pan=0, decay=0.1|
	var snd, randomA, randomB;
	randomA = LFNoise2.kr(Rand(10, 50), freq/10);
	randomB = LFNoise2.kr(Rand(50,200), 0.5, 0.5);
	snd = Crackle.ar(1) * randomB;
	snd = RHPF.ar(snd, freq/2 + randomA, rq);
	snd = RLPF.ar(snd, freq*2 +  randomA, rq);
	snd = Ringz.ar(snd, freq + randomA, decay) * randomB * 10;
	snd = snd * EnvGen.kr(Env.adsr(0.1, 3, 0.1, 1, 0.3 ), gate, doneAction:2);
	snd = Pan2.ar(snd, pan, amp * 1.75);
	Out.ar(out, snd);
}).store;

SynthDef(\bubGreen, { |out, freq=440, rq=0.05, dur=1, amp=0.01, gate=1, pan=0, decay=0.1|
	var snd, random;
	random = LFNoise2.kr(Rand(10,50), freq/10);
	snd = WhiteNoise.ar(1) * LFNoise2.ar(111, 0.1);
	snd = HPF.ar(snd, freq/2 + random, freq/10);
	snd = RLPF.ar(snd, freq*2 + random, rq);
	snd = snd * EnvGen.kr(Env.adsr(0.1, 3, 0.1, 1, 0.3 ), gate, doneAction:2);
	snd = Pan2.ar(snd, pan, amp * 0.4);
	Out.ar(out, snd);
}).store;


SynthDef(\bubYel, { |out, freq=440, rq=0.01, dur=1, amp=0.01, gate=1, pan=0, decay=0.1|
	var snd, random;
	random = LFNoise2.kr(Rand(10,100), freq/20);
	snd = PinkNoise.ar(1);
	snd = RHPF.ar(snd, freq/4 + random, rq) * 3;
	snd = RLPF.ar(snd, freq*4 + random, rq) * 3;
	snd = RHPF.ar(snd, freq/10 + random) * 3;
	snd = Resonz.ar(snd, freq + random, rq*20);
	snd = snd * EnvGen.kr(Env.adsr(0.1, 3, 0.1, 1, 0.3 ), gate, doneAction:2);
	snd = Pan2.ar(snd, pan, amp * 2 * LFNoise2.kr(Rand(1, 10), 0.2, 0.8) );
	Out.ar(out, snd);
}).store;

SynthDef(\bubLila, { |out, freq=1000, rq=0.5, dur=1, amp=0.01, gate=1, pan=0, decay=0.1|
	var snd;
	snd = Blip.ar(SinOsc.kr([100,200,300,400], 0, freq/10, freq), LFNoise2.kr(Rand(10,50), 8, 12), LFNoise2.kr(Rand(20,200), 0.5, 0.5)).sum;
	snd = snd * EnvGen.kr(Env.adsr(0.1, 3, 0.1, 1, 0.3 ), gate, doneAction:2);
	snd = Pan2.ar(snd, pan, amp * 2 * LFNoise2.kr(Rand(1, 10), 0.2, 0.8) );
	Out.ar(out, snd);
}).store;

SynthDef(\bubRed, { |out, freq=440, rq=0.5, dur=1, amp=0.01, gate=1, pan=0, decay=0.1|
	var snd;
	snd = Saw.ar(SinOsc.kr(200, 0, freq/10, freq + LFTri.kr(Rand(1,20), mul:freq/10)), SinOsc.kr(50));
	snd = snd * EnvGen.kr(Env.adsr(0.1, 3, 0.1, 1, 0.3 ), gate, doneAction:2);
	snd = Pan2.ar(snd, pan, amp * 2 * LFNoise2.kr(Rand(1, 10), 0.2, 0.8) );
	Out.ar(out, snd);
}).store;

SynthDef(\bubOrange, { |out, freq=400, rq=0.5, dur=1, amp=0.01, gate=1, pan=0, decay=0.1|
	var snd;
	snd = Formant.ar(
			SinOsc.kr(freq/100).range(freq-(freq/50), freq+(freq/50)),
			SinOsc.kr(freq/20).range(freq/10, freq*10),
			SinOsc.kr(freq/100).range(freq-(freq/10), freq+(freq/15))
	);
	snd = snd * EnvGen.kr(Env.adsr(0.1, 3, 0.1, 1, 0.3 ), gate, doneAction:2);
	snd = Pan2.ar(snd, pan, amp * 3 * LFNoise2.kr(Rand(1, 10), 0.2, 0.8) );
	Out.ar(out, snd);
}).store;

//---SYNTHS EVENTS

SynthDef(\fire, { |out, freq=400, dur=0.25, amp=0.025, pan=0, decay=0.1|
	var snd, noise;
	noise = Decay2.ar(Impulse.ar(0), 0.05, 1);
	snd = SinOsc.ar(SinOsc.kr(Rand(2,30), 0, freq/5, [freq, freq*1.5]) + EnvGen.kr(Env.sine(dur), 1, Rand(freq, freq*2))).sum;
	snd = snd + noise * EnvGen.ar(Env.perc(0,dur), doneAction:2);
	snd = Pan2.ar(snd, pan, amp);
	OffsetOut.ar(out, snd);
}).store;

SynthDef(\dir, { |out, freq=2000, dur=0.25, amp=0.005, pan=0, rq=0.001|
	var snd;
	snd = RHPF.ar(WhiteNoise.ar, Rand(freq, freq*1.5), rq);
	snd = snd * EnvGen.ar(Env.perc(0.001,0.02), doneAction:2);
	snd = Pan2.ar(snd, pan, amp);
	Out.ar(out, snd);
}).store;

SynthDef(\rebound, { |out, freq=1000, dur=0.25, amp=0.0075, pan=0, decay=0.1|
	var snd, noise;
	snd = Klank.ar(`[
		Array.fill(20, { |i| freq + (i*2) + Rand(0, freq*5) }), nil, 1!20],
		Impulse.ar(0)
	);
	snd = snd * EnvGen.ar(Env.perc(0.05, 3), doneAction:2);
	snd = Pan2.ar(snd, pan, amp);
	OffsetOut.ar(out, snd);
}).store;


SynthDef(\hurry, { |out, freq=60, dur=0.25, amp=0.01, pan=0, decay=0.1|
	var snd, noise;
	snd = SinOsc.ar(
		[freq, freq*1.01+Rand(0,2), freq*2+Rand(0,5), freq*2.054+Rand(0,10)]
	).sum;
	snd = snd * EnvGen.ar(Env.sine(2), doneAction:2);
	snd = Pan2.ar(snd, pan, amp);
	OffsetOut.ar(out, snd);
}).store;

SynthDef(\stick, { |out, freq=400, dur=0.1, amp=0.01, pan=0, decay=0.1|
	var snd, noise;
	snd = SinOsc.ar(freq+Rand(0,100));
	snd = snd * EnvGen.ar(Env.perc(0,dur), doneAction:2);
	snd = Pan2.ar(snd, pan, amp);
	OffsetOut.ar(out, snd);
}).store;

SynthDef(\fall, { |out, freq=400, dur=0.75, amp=0.01|
	var snd;
	snd = Dust.ar(Rand(3, 25)) * 5;
	snd = RHPF.ar(snd, Rand(400,15000), Rand(0.1,0.9));
	snd = snd * EnvGen.ar(Env.perc(0,dur + Rand(0.0,1.0)), doneAction:2);
	snd = Pan2.ar(snd, Rand(-0.5, 0.5), amp);
	OffsetOut.ar(out, snd);
}).store;

SynthDef(\move, { |out, dur=3, amp=0.0075|
	var snd, line, freq;
	freq = Rand(60,120);
	line = XLine.kr(freq, freq/2, dur);
	snd = Pulse.ar(line, Rand(0.1,0.9));
	snd = RLPF.ar(snd, line, Rand(0.3,0.7)) * 10;
	snd = snd * EnvGen.ar(Env.sine(dur), doneAction:2);
	snd = Pan2.ar(snd, Rand(-0.1, 0.1), amp);
	OffsetOut.ar(out, snd);
}).store;

//---Main Effects

SynthDef(\reverbz, { |in=0, out=0, amp=0.1, mix=0.33, room=1, damp=1|
	var snd;
	snd = In.ar(in, 2);
	snd = FreeVerb2.ar(snd[0], snd[1], mix, room, damp);
	ReplaceOut.ar(out, snd);
}).store;

SynthDef(\compressor, { |in=0, out=0, amp=5, thresh=0.5, slopeBelow=0.9, slopeAbove=0.1, clampTime=0.01, relaxTime=0.01|
	var snd;
	snd = In.ar(in, 2);
	snd = Compander.ar(snd, snd, thresh, slopeBelow, slopeAbove, clampTime, relaxTime);
	ReplaceOut.ar(out, snd * amp);
}).store;

//---OSC Responders

OSCresponder(nil, "/fb/state/init", {|t, r, msg|
	"Recieving OSC messages from Frozen Bubble OSCfied".postln;
}).add;

OSCresponder(nil, "/fb/state/won", {|t, r, msg|
	msg.postln;
}).add;

OSCresponder(nil, "/fb/state/lost", {|t, r, msg|
	msg.postln;
	q.bub.do{ |item|
		( item.isPlaying ).if (
			{ item.set(\gate, 0);
			  q.bub.removeAt(q.bub.findKeyForValue(item))
			}
		)
	};	
}).add;

OSCresponder(nil, "/fb/event/movedown", {|t, r, msg|
	Synth(\move)
}).add;

OSCresponder(nil, "/fb/event/fire", {|t, r, msg|
	Synth(\fire);
}).add;

OSCresponder(nil, "/fb/event/hurry", {|t, r, msg|
	Synth(\hurry, [\freq, ([35,38,40,41,45,48]).choose.midicps])
}).add;

OSCresponder(nil, "/fb/event/new", {|t, r, msg|
	msg.postln;
	q.bub.do{ |item|
		( item.isPlaying ).if (
			{ item.set(\gate, 0);
			  q.bub.removeAt(q.bub.findKeyForValue(item))
			}
		)
	};	
}).add;

OSCresponder(nil, "/fb/state/splash", {|t, r, msg|
	msg.postln;
	q.bub.do{ |item|
		( item.isPlaying ).if (
			{ item.set(\gate, 0);
			  q.bub.removeAt(q.bub.findKeyForValue(item))
			}
		)
	};	
	s.freeAll;
	q.freq = 40;
	q.factor = 50;
	q.compressor = Synth.tail(s, \compressor);	
	q.reverb = Synth.tail(s, \reverbz);
}).add;

OSCresponder(nil, "/fb/level", {|t, r, msg|
	q.freq = q.freq + (msg[1] * 10);
	q.factor = q.factor + msg[1];
}).add;

OSCresponder(nil, "/fb/direction", {|t, r, msg|
	Synth(\dir, [\pan, msg[1], \freq, 1000 + (q.freq*5)])
}).add;


//...BUBBLES
OSCresponder(nil, "/fb/bubble/spawn", {|t, r, msg|
	( msg[2] == 0).if( { q.bub[msg[1]] =
		Synth(\bubBlack,
			[ \freq, q.freq + (msg[1]*q.factor), \pan, msg[3] * 2 -1, \amp, (1 - msg[4] * 0.01).abs ]
		).register;
	});

	( msg[2] == 1).if( { q.bub[msg[1]] =
		Synth(\bubSilver,
			[ \freq, q.freq + (msg[1]*q.factor), \pan, msg[3] * 2 -1, \amp, (1 - msg[4] * 0.01).abs ]
		).register; 
	});

	( msg[2] == 2).if( { q.bub[msg[1]] =
		Synth(\bubBlue,
			[ \freq, q.freq + (msg[1]*q.factor), \pan, msg[3] * 2 -1, \amp, (1 - msg[4] * 0.01).abs ]
		).register;
	});

	( msg[2] == 3).if( { q.bub[msg[1]] =
		Synth(\bubGreen,
			[ \freq, q.freq + (msg[1]*q.factor), \pan, msg[3] * 2 -1, \amp, (1 - msg[4] * 0.01).abs ]
		).register;
	});

	( msg[2] == 4).if( { q.bub[msg[1]] =
		Synth(\bubYel,
			[ \freq, q.freq + (msg[1]*q.factor), \pan, msg[3] * 2 -1, \amp, (1 - msg[4] * 0.01).abs ]
		).register;
	});

	( msg[2] == 5).if( { q.bub[msg[1]] =
		Synth(\bubLila,
			[ \freq, q.freq + (msg[1]*q.factor), \pan, msg[3] * 2 -1, \amp, (1 - msg[4] * 0.01).abs ]
		).register;
	});

	( msg[2] == 6).if( { q.bub[msg[1]] =
		Synth(\bubRed,
			[ \freq, q.freq + (msg[1]*q.factor), \pan, msg[3] * 2 -1, \amp, (1 - msg[4] * 0.01).abs ]
		).register;
	});

	( msg[2] == 7).if( { q.bub[msg[1]] =
		Synth(\bubOrange,
			[ \freq, q.freq + (msg[1]*q.factor), \pan, msg[3] * 2 -1, \amp, (1 - msg[4] * 0.01).abs  ]
		).register;
	});
}).add;

OSCresponder(nil, "/fb/bubble/position", {|t, r, msg|
	q.bub[msg[1]].set(\pan, msg[2] * 2 -1 )
}).add;

OSCresponder(nil, "/fb/bubble/rebound", {|t, r, msg|
	msg.postln;
	Synth(\rebound, [\pan, msg[2], \amp, msg[3] * 0.01])
}).add;

OSCresponder(nil, "/fb/bubble/frozenify", {|t, r, msg|
	q.bub[msg[1]].set(\gate, 0);
	q.bub.removeAt(msg[1]);
}).add;

OSCresponder(nil, "/fb/bubble/stick", {|t, r, msg|
	Synth(\stick, [\freq, msg[1]*20])
}).add;

OSCresponder(nil, "/fb/bubble/fall", {|t, r, msg|
	q.bub[msg[1]].set(\gate, 0);
	Synth(\fall);
}).add;


OSCresponder(nil, "/fb/bubble/jump", {|t, r, msg|
	q.bub[msg[1]].set(\gate, 0);
	Synth(\fall);
}).add;


OSCresponder(nil, "/fb/bubble/delete", {|t, r, msg|
	q.bub.removeAt(msg[1]);
}).add;

)
// jgar