package com.coalesce;

import com.coalesce.type.Switch;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class SwitchTests {

	private static Switch aSwitch;

	@BeforeClass
	public static void create() {
		aSwitch = new Switch() {

			private boolean isEnabled;

			@Override
			public void enable() {
				isEnabled = true;
			}

			@Override
			public void disable() {
				isEnabled = false;
			}

			@Override
			public boolean isEnabled() {
				return isEnabled;
			}

		};
	}

	@Test
	public void test1Enable() {
		aSwitch.enable();
	}

	@Test
	public void test2IsEnabled() {
		assertTrue("Switch wasn't properly enabled", aSwitch.isEnabled());
	}

	@Test
	public void test3Disable() {
		aSwitch.disable();
	}

	@AfterClass
	public static void destroy() {
		aSwitch = null;
	}

}
