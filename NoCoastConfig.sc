/**********

NoCoastConfig

A Quark for configuring the MakeNoise 0-Coast.

NOTE: This REQUIRES firmware v1.5.4 installed on the 0-Coast.

Since v1.5.4, the 0-Coast has been configurable using MIDI CC messages.
This avoids digging around in the PGM pages!

This class is simply a wrapper around the MakeNoise specification, with
a simple GUI to make things easy.

James Surgenor, 2017

**********/

NoCoastConfig
{
	var m_midiout;

	*new {
		^super.new.init;
	}

	init {
		MIDIClient.init;
		this.makeWindow;
	}

	makeWindow {
		var win, mainLayout;

		win = Window("0-Coast Setup");
		mainLayout = GridLayout();

		mainLayout.vSpacing_(15);
		mainLayout.setColumnStretch(0,1);
		mainLayout.setColumnStretch(1,1);

		mainLayout.addSpanning(
			PopUpMenu()
			.items_(MIDIClient.destinations.collect(_.device))
			.action_({ | caller |
				this.setDevice(caller.value);
			})
		,0,0,1,2,\center);

		mainLayout.add(
			Button()
			.states_([["Arp On"],["Arp Off"]])
			.action_({ | caller |
				if(caller.value == 0)
				{
					this.arpOff;
				}
				{
					this.arpOn;
				}
			})
		,1, 0);

		mainLayout.add(
			Button()
			.states_([["Arp Latch"],["Arp Trad"]])
			.action_({ | caller |
				if(caller.value == 0)
				{
					this.arpTrad;
				}
				{
					this.arpLatch;
				}
			})
		,1, 1);

		mainLayout.addSpanning(
			Button()
			.states_([["Legato On"],["Legato Off"]])
			.action_({ | caller |
				if(caller.value == 0)
				{
					this.legatoOff;
				}
				{
					this.legatoOn;
				}
			})
		,2, 0, 1, 2, \center);


		mainLayout.add(StaticText().string_("MIDI Ch1: "), 3, 0);
		mainLayout.add(NumberBox().clipLo_(0).clipHi_(127).action_({| caller |
			this.midiCh1(caller.value);
		}), 3, 1);

		mainLayout.add(StaticText().string_("MIDI Ch2: "), 4, 0);
		mainLayout.add(NumberBox().clipLo_(0).clipHi_(127).action_({| caller |
			this.midiCh2(caller.value);
		}), 4, 1);

		mainLayout.add(StaticText().string_("MIDI A CV: "), 5, 0);
		mainLayout.add(PopUpMenu().items_(["Note", "Vel", "Mod Wheel", "LFO"]).action_({ | caller |
			switch( caller.value,
				0, { this.midiChA_CV_Note; },
				1, { this.midiChA_CV_Vel; },
				2, { this.midiChA_CV_Mod; },
				3, { this.midiChA_CV_LFO; }
			)
		}), 5, 1);

		mainLayout.add(StaticText().string_("MIDI A Gate: "), 6, 0);
		mainLayout.add(PopUpMenu().items_(["Note", "Vel", "Mod Wheel", "LFO"]).action_({ | caller |
			switch( caller.value,
				0, { this.midiChA_Gate_Note; },
				1, { this.midiChA_Gate_Vel; },
				2, { this.midiChA_Gate_Mod; },
				3, { this.midiChA_Gate_LFO; }
			)
		}), 6, 1);

		mainLayout.add(StaticText().string_("MIDI B CV: "), 7, 0);
		mainLayout.add(PopUpMenu().items_(["Note", "Vel", "Mod Wheel", "LFO"]).action_({ | caller |
			switch( caller.value,
				0, { this.midiChB_CV_Note; },
				1, { this.midiChB_CV_Vel; },
				2, { this.midiChB_CV_Mod; },
				3, { this.midiChB_CV_LFO; }
			)
		}), 7, 1);

		mainLayout.add(StaticText().string_("MIDI B Gate: "), 8, 0);
		mainLayout.add(PopUpMenu().items_(["Note", "Vel", "Mod Wheel", "LFO"]).action_({ | caller |
			switch( caller.value,
				0, { this.midiChB_Gate_Note; },
				1, { this.midiChB_Gate_Vel; },
				2, { this.midiChB_Gate_Mod; },
				3, { this.midiChB_Gate_LFO; }
			)
		}), 8, 1);

		mainLayout.add(StaticText().string_("MIDI A PitchBend Scale: "), 9, 0);
		mainLayout.add(NumberBox().clipLo_(0).clipHi_(127).action_({| caller |
			this.midiChA_PitchBendScale(caller.value);
		}), 9, 1);

		mainLayout.add(StaticText().string_("MIDI B PitchBend Scale: "), 10, 0);
		mainLayout.add(NumberBox().clipLo_(0).clipHi_(127).action_({| caller |
			this.midiChB_PitchBendScale(caller.value);
		}), 10, 1);


		mainLayout.add(StaticText().string_("MIDI A AfterTouch Scale: "), 11, 0);
		mainLayout.add(NumberBox().clipLo_(0).clipHi_(127).action_({| caller |
			this.midiChA_AfterScale(caller.value);
		}), 11, 1);

		mainLayout.add(StaticText().string_("MIDI B AfterTouch Scale: "), 12, 0);
		mainLayout.add(NumberBox().clipLo_(0).clipHi_(127).action_({| caller |
			this.midiChB_AfterScale(caller.value);
		}), 12, 1);


		mainLayout.add(StaticText().string_("MIDI A Vel Scale: "), 13, 0);
		mainLayout.add(NumberBox().clipLo_(0).clipHi_(127).action_({| caller |
			this.midiChA_VelScale(caller.value);
		}), 13, 1);

		mainLayout.add(StaticText().string_("MIDI B Vel Scale: "), 14, 0);
		mainLayout.add(NumberBox().clipLo_(0).clipHi_(127).action_({| caller |
			this.midiChB_VelScale(caller.value);
		}), 14, 1);

		mainLayout.addSpanning(
			Button()
			.states_([["MIDI Clock On"],["MIDI Clock Off"]])
			.action_({ | caller |
				if(caller.value == 0)
				{
					this.midiClkOff;
				}
				{
					this.midiClkOn;
				}
			})
		,15, 0, 1, 2, \center);

		mainLayout.add(StaticText().string_("TempoIn Division: "), 16, 0);
		mainLayout.add(NumberBox().clipLo_(0).clipHi_(127).action_({| caller |
			this.tempoInDiv(caller.value);
		}), 16, 1);

		win.layout_(mainLayout);
		win.front;
	}


	/*
	** PRIVATE(ISH) FUNCTIONS
	*/

	/////

	setDevice { | id |
		m_midiout.free;
		m_midiout = MIDIOut(id);
	}

	/////

	arpOn{
		m_midiout.control(0, 117, 1);
	}

	arpOff{
		m_midiout.control(0, 117, 0);
	}

	arpTrad{
		m_midiout.control(0, 119, 0);
	}

	arpLatch{
		m_midiout.control(0, 119, 1);
	}

	/////

	legatoOn{
		m_midiout.control(0, 118, 1);
	}

	legatoOff{
		m_midiout.control(0, 118, 0);
	}

	/////

	midiCh1{ | chan=0 |
		m_midiout.control(0, 102, chan);
		if(chan > 15)
		{
			"MIDI 1: CH ALL".warn;
		};
	}

	midiCh2{ | chan=0 |
		m_midiout.control(0, 102, chan);
		if(chan > 15)
		{
			"MIDI 2: CH ALL".warn;
		};
	}

	/////

	midiChA_CV_Note{
		m_midiout.control(0, 104, 0);
	}
	midiChA_CV_Vel{
		m_midiout.control(0, 104, 1);
	}
	midiChA_CV_Mod{
		m_midiout.control(0, 104, 2);
	}
	midiChA_CV_LFO{
		m_midiout.control(0, 104, 3);
	}

	midiChB_CV_Note{
		m_midiout.control(0, 105, 0);
	}
	midiChB_CV_Vel{
		m_midiout.control(0, 105, 1);
	}
	midiChB_CV_Mod{
		m_midiout.control(0, 105, 2);
	}
	midiChB_CV_LFO{
		m_midiout.control(0, 105, 3);
	}

	/////

	midiChA_Gate_Note{
		m_midiout.control(0, 106, 0);
	}
	midiChA_Gate_Vel{
		m_midiout.control(0, 106, 1);
	}
	midiChA_Gate_Mod{
		m_midiout.control(0, 106, 2);
	}
	midiChA_Gate_LFO{
		m_midiout.control(0, 106, 3);
	}

	midiChB_Gate_Note{
		m_midiout.control(0, 107, 0);
	}
	midiChB_Gate_Vel{
		m_midiout.control(0, 107, 1);
	}
	midiChB_Gate_Mod{
		m_midiout.control(0, 107, 2);
	}
	midiChB_Gate_LFO{
		m_midiout.control(0, 107, 3);
	}

	/////

	midiChA_PitchBendScale{ | val |
		m_midiout.control(0, 108, val);
	}

	midiChB_PitchBendScale{ | val |
		m_midiout.control(0, 109, val);
	}

	/////

	midiChA_AfterScale{ | val |
		m_midiout.control(0, 110, val);
	}

	midiChB_AfterScale{ | val |
		m_midiout.control(0, 111, val);
	}

	/////

	midiChA_VelScale{ | val |
		m_midiout.control(0, 112, val);
	}

	midiChB_VelScale{ | val |
		m_midiout.control(0, 113, val);
	}

	/////

	midiClkOn{
		m_midiout.control(0, 114, 1);
	}

	midiClkOff{
		m_midiout.control(0, 114, 0);
	}

	/////

	tempoInDiv{ | val |
		m_midiout.control(0, 116, val);
	}
}