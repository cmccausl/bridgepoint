
//=====================================================================
//
//File:      $RCSfile: CanvasSuite1.java,v $
//Version:   $Revision: 1.3 $
//Modified:  $Date: 2013/05/10 05:47:48 $
//
//(c) Copyright 2007-2014 by Mentor Graphics Corp. All rights reserved.
//
//=====================================================================
// Licensed under the Apache License, Version 2.0 (the "License"); you may not 
// use this file except in compliance with the License.  You may obtain a copy 
// of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   See the 
// License for the specific language governing permissions and limitations under
// the License.
//=====================================================================


import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.CoreException;

import org.xtuml.bp.core.CorePlugin;
import org.xtuml.bp.core.common.BridgePointPreferencesStore;
import org.xtuml.bp.core.util.WorkspaceUtil;
import org.xtuml.bp.ui.canvas.test.CanvasCreationTest;
import org.xtuml.bp.ui.canvas.test.CanvasEditorReloadContentsTest;
import org.xtuml.bp.ui.canvas.test.CanvasEditorTestSuite;
import org.xtuml.bp.ui.canvas.test.CanvasInitialNameTests;
import org.xtuml.bp.ui.canvas.test.CanvasTestSuite1;
import org.xtuml.bp.ui.canvas.test.CreationTransitionTest;
import org.xtuml.bp.ui.canvas.test.ErrorPathsTest;
import org.xtuml.bp.ui.canvas.test.ListenerTest;
import org.xtuml.bp.ui.canvas.test.ODMSTest;
import org.xtuml.bp.ui.canvas.test.ShapeResizeTest;
import org.xtuml.bp.ui.canvas.test.SymbolTest;

/**
 * Test all areas of the canvas
 */
public class CanvasSuite1 extends TestSuite {

	/**
	 * Returns the suite.  This is required to
	 * use the JUnit Launcher.
	 */
	public static Test suite() {
		return new CanvasSuite1();
	}
	
	/**
	 * Construct the test suite.
	 */
	public CanvasSuite1() {
		
		// turn off autobuild
		try {
			WorkspaceUtil.setAutobuilding(false);
		} catch (CoreException e) {
			Assert.fail(e.toString());
		}
		CorePlugin.getDefault().getPreferenceStore().setValue(BridgePointPreferencesStore.USE_DEFAULT_NAME_FOR_CREATION,true);
		
		addTest(new TestSuite(CanvasTestSuite1.class));
		addTest(new TestSuite(ErrorPathsTest.class));
        addTest(new TestSuite(CanvasInitialNameTests.class));
		addTest(new TestSuite(SymbolTest.class));
		addTest(new TestSuite(ODMSTest.class));
        addTest(new TestSuite(CanvasEditorReloadContentsTest.class));
		addTest(new TestSuite(ListenerTest.class));
		addTest(new TestSuite(CanvasCreationTest.class));	
        addTest(new TestSuite(ShapeResizeTest.class));
		addTest(new CanvasEditorTestSuite());
		addTest(new TestSuite(CreationTransitionTest.class));
		//addTest(new TestSuite(I634OutlineViewEmptySelectionTest.class));
	}

}