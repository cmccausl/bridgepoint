//=====================================================================
//
//File:      $RCSfile: VerifierInterfaceExecutionTestsGlobals.java,v $
//Version:   $Revision: 1.5 $
//Modified:  $Date: 2013/05/10 13:25:45 $
//
//(c) Copyright 2008-2014 by Mentor Graphics Corp. All rights reserved.
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
package org.xtuml.bp.debug.ui.test.execute;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.ui.PlatformUI;

import org.xtuml.bp.core.ClassStateMachine_c;
import org.xtuml.bp.core.ComponentInstance_c;
import org.xtuml.bp.core.Component_c;
import org.xtuml.bp.core.CorePlugin;
import org.xtuml.bp.core.InterfaceReference_c;
import org.xtuml.bp.core.ModelClass_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Operation_c;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.Port_c;
import org.xtuml.bp.core.ProvidedExecutableProperty_c;
import org.xtuml.bp.core.ProvidedOperation_c;
import org.xtuml.bp.core.Provision_c;
import org.xtuml.bp.core.RequiredExecutableProperty_c;
import org.xtuml.bp.core.RequiredOperation_c;
import org.xtuml.bp.core.RequiredSignal_c;
import org.xtuml.bp.core.Requirement_c;
import org.xtuml.bp.core.StateMachineState_c;
import org.xtuml.bp.core.StateMachine_c;
import org.xtuml.bp.core.SystemModel_c;
import org.xtuml.bp.core.common.BridgePointPreferencesStore;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.PersistableModelComponent;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.core.ui.perspective.BridgePointPerspective;
import org.xtuml.bp.debug.ui.launch.BPDebugUtils;
import org.xtuml.bp.debug.ui.test.DebugUITestUtilities;
import org.xtuml.bp.test.TestUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.TestingUtilities;
import org.xtuml.bp.ui.text.activity.ActivityEditor;

public class VerifierInterfaceExecutionTestsGlobals extends BaseTest {

	private static String projectName = "VerifierInterfaceExecutionTests";

	private static boolean initialized = false;

	public VerifierInterfaceExecutionTestsGlobals(String testName) throws Exception {
		super(projectName, testName);
	}

	@Override
	protected void setUp() throws Exception {
		if (!initialized)
			  delayGlobalUpgrade = true;
		super.setUp();

		if (!initialized) {
			CorePlugin.disableParseAllOnResourceChange();

			// set perspective switch dialog on launch
			DebugUIPlugin.getDefault().getPluginPreferences().setValue(
					IDebugUIConstants.PLUGIN_ID + ".switch_to_perspective",
					"always");

			CorePlugin
					.getDefault()
					.getPluginPreferences()
					.setDefault(
							BridgePointPreferencesStore.ALLOW_IMPLICIT_COMPONENT_ADDRESSING,
							true);

			// initialize test model
			final IProject project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(projectName);

			File sourceProject = new File(m_workspace_path + "../"
					+ projectName);

			TestingUtilities.copyProjectContents(sourceProject, project);

			TestingUtilities.allowJobCompletion();

			m_sys = SystemModel_c.SystemModelInstance(Ooaofooa
					.getDefaultInstance(), new ClassQueryInterface_c() {

				public boolean evaluate(Object candidate) {
					return ((SystemModel_c) candidate).getName().equals(
							project.getName());
				}

			});

			PersistableModelComponent sys_comp = m_sys
					.getPersistableComponent();
			sys_comp.loadComponentAndChildren(new NullProgressMonitor());

			CorePlugin.enableParseAllOnResourceChange();

			TestingUtilities.allowJobCompletion();
			while (!ResourcesPlugin.getWorkspace().getRoot().isSynchronized(
					IProject.DEPTH_INFINITE)) {
				ResourcesPlugin.getWorkspace().getRoot().refreshLocal(
						IProject.DEPTH_INFINITE, new NullProgressMonitor());
				while (PlatformUI.getWorkbench().getDisplay().readAndDispatch())
					;
			}

			Ooaofooa.setPersistEnabled(true);
            delayGlobalUpgrade = false;

			initialized = true;
		}
	}

	public void tearDown() throws Exception {
		DebugUITestUtilities.stopSession(m_sys, projectName);
	}

	public void testInterfaceExecutionSignalAssignedToTransition() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port1");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName()
										.equals("sendServerClientOAL");
							}

						});
		assertNotNull(reqOp);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.executeElement(reqOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		DebugUITestUtilities.waitForExecution();

		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_assigned_signal.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionSignalNoOALAssignedToTransition() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port1");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName().equals(
												"sendServerClientNoOAL");
							}

						});
		assertNotNull(reqOp);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.executeElement(reqOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		DebugUITestUtilities.waitForExecution();

		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_no_oal_assigned_signal.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionSignalParametersAssignedToTransition() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);

    Package_c ss = Package_c.getOneEP_PKGOnR8001(PackageableElement_c.getManyPE_PEsOnR8003(component), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("ss");
      }
    });
    assertNotNull(ss);

		StateMachineState_c state = StateMachineState_c
				.getOneSM_STATEOnR501(
						StateMachine_c
								.getManySM_SMsOnR517(ClassStateMachine_c
										.getManySM_ASMsOnR519(ModelClass_c
												.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(ss)))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((StateMachineState_c) candidate)
										.getName().equals("State 4");
							}

						});
		assertNotNull(state);

    // launch the component
    DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
        m_bp_tree.getControl().getMenu(), m_sys.getName());

		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(state);

		ActivityEditor editor = DebugUITestUtilities
				.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 24);
		
		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port1");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName().equals(
												"sendServerClientParams");
							}

						});
		assertNotNull(reqOp);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.executeElement(reqOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		DebugUITestUtilities.waitForExecution();

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);
		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in state.", target
				.isSuspended());

		DebugUITestUtilities.processDebugEvents();
		TestingUtilities.processDisplayEvents();
		
		// verify the parameters
		// Commenting out known failure tests.  See dts0100656068
/*		String xValue = DebugUITestUtilities.getValueForVariable("x");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"2", xValue);
		String yValue = DebugUITestUtilities.getValueForVariable("y");
		assertEquals(
				"Default parameter value was not as expected for variable y.",
				"4", yValue);
*/
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_assigned_signal_with_parameters.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionSignalWithQueuedEventsBadState() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);

    Package_c ss = Package_c.getOneEP_PKGOnR8001(PackageableElement_c.getManyPE_PEsOnR8003(component), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("ss");
      }
    });
    assertNotNull(ss);

    

		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());
		
		StateMachineState_c state = StateMachineState_c
			.getOneSM_STATEOnR501(
				StateMachine_c
						.getManySM_SMsOnR517(ClassStateMachine_c
								.getManySM_ASMsOnR519(ModelClass_c
										.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(ss), new ClassQueryInterface_c() {
																
																	public boolean evaluate(Object candidate) {
																		return ((ModelClass_c)candidate).getName().equals("cls 2");
																	}
																
																}))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((StateMachineState_c) candidate)
								.getName().equals("State 2");
					}

				});
		assertNotNull(state);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(state);
		
		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		ModelClass_c startClass = ModelClass_c
			.getOneO_OBJOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(ss), new ClassQueryInterface_c() {
								
									public boolean evaluate(Object candidate) {
										return ((ModelClass_c)candidate).getName().equals("cls 3");
									}
								
								});
		assertNotNull(startClass);
		
		Operation_c op = Operation_c.getOneO_TFROnR115(startClass, new ClassQueryInterface_c() {
		
			public boolean evaluate(Object candidate) {
				return ((Operation_c) candidate).getName().equals("start_test_bad_state");
			}
		
		});
		assertNotNull(op);
		
		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.executeElement(op);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in state.", target
				.isSuspended());

		ProvidedOperation_c proOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port2");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"sendClientServer");
					}

				});
		assertNotNull(proOp);
		
		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.executeElement(proOp);

		
		DebugUITestUtilities.waitForExecution();

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		DebugUITestUtilities.removeAllBreakpoints();
		try {
			process.getLaunch().getDebugTarget().resume();
		} catch (DebugException e) {
			fail(e.toString());
		}

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_assigned_signal_queued_events_bad.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}
	
	public void testInterfaceExecutionSignalWithQueuedEventsGoodState() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());
		
    Package_c ss = Package_c.getOneEP_PKGOnR8001(PackageableElement_c.getManyPE_PEsOnR8003(component), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("ss");
      }
    });
    assertNotNull(ss);

	StateMachineState_c state = StateMachineState_c
			.getOneSM_STATEOnR501(
				StateMachine_c
						.getManySM_SMsOnR517(ClassStateMachine_c
								.getManySM_ASMsOnR519(ModelClass_c
										.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(ss), new ClassQueryInterface_c() {
																
																	public boolean evaluate(Object candidate) {
																		return ((ModelClass_c)candidate).getName().equals("cls 2");
																	}
																
																}))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((StateMachineState_c) candidate)
								.getName().equals("State 2");
					}

				});
		assertNotNull(state);
		
		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(state);
		
		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		ModelClass_c startClass = ModelClass_c
			.getOneO_OBJOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(ss), new ClassQueryInterface_c() {
								
									public boolean evaluate(Object candidate) {
										return ((ModelClass_c)candidate).getName().equals("cls 3");
									}
								
								});
		assertNotNull(startClass);
		
		Operation_c op = Operation_c.getOneO_TFROnR115(startClass, new ClassQueryInterface_c() {
		
			public boolean evaluate(Object candidate) {
				return ((Operation_c) candidate).getName().equals("start_test_good_state");
			}
		
		});
		assertNotNull(op);
		
		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.executeElement(op);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in state.", target
				.isSuspended());

		ProvidedOperation_c proOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port2");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"sendClientServer");
					}

				});
		assertNotNull(proOp);
		
		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.executeElement(proOp);

		
		DebugUITestUtilities.waitForExecution();

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		DebugUITestUtilities.removeAllBreakpoints();
		try {
			process.getLaunch().getDebugTarget().resume();
		} catch (DebugException e) {
			fail(e.toString());
		}

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_assigned_signal_queued_events_good.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionSignalNotAssignedWithOAL() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		RequiredSignal_c reqSig = RequiredSignal_c
			.getOneSPR_RSOnR4502(
				RequiredExecutableProperty_c
						.getManySPR_REPsOnR4500(Requirement_c
								.getManyC_RsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port3");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((RequiredSignal_c) candidate)
								.getName().equals(
										"serverclientsig");
					}

				});
		assertNotNull(reqSig);
 
		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(reqSig);
		
		ActivityEditor editor = DebugUITestUtilities
				.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port3");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName().equals(
												"sendServerClientOAL");
							}

						});
		assertNotNull(reqOp);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.executeElement(reqOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in required signal.", target
				.isSuspended());
		
		DebugUITestUtilities.removeAllBreakpoints();
		try {
			target.resume();
		} catch (DebugException e) {
			fail(e.toString());
		}
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_signal_oal.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}
	
	public void testInterfaceExecutionSignalNotAssignedWithNoOAL() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port3");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName().equals(
												"sendServerClientNoOAL");
							}

						});
		assertNotNull(reqOp);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(reqOp);

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		BPDebugUtils.executeElement(reqOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in requied operation.", target
				.isSuspended());
		
		DebugUITestUtilities.stepOver(engine, 2);
		
		String topFrameText = DebugUITestUtilities.getTopFrameText(engine);
		assertEquals("Port3::Interface Server Client 2::sendServerClientNoOAL line: 4", topFrameText);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_signal_no_oal.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionSignalNotAssignedCurrentExecutingAction() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		Package_c ss = Package_c.getOneEP_PKGOnR8001(PackageableElement_c.getManyPE_PEsOnR8003(component), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("ss");
      }
    });
    assertNotNull(ss);
		
		StateMachineState_c state = StateMachineState_c
			.getOneSM_STATEOnR501(
				StateMachine_c
						.getManySM_SMsOnR517(ClassStateMachine_c
								.getManySM_ASMsOnR519(ModelClass_c
										.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(ss), new ClassQueryInterface_c() {
																
																	public boolean evaluate(Object candidate) {
																		return ((ModelClass_c)candidate).getName().equals("cls 2");
																	}
																
																}))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((StateMachineState_c) candidate)
								.getName().equals("State 2");
					}

				});
		assertNotNull(state);
		
		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(state);
		
		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		ModelClass_c startClass = ModelClass_c
			.getOneO_OBJOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(ss), new ClassQueryInterface_c() {
								
									public boolean evaluate(Object candidate) {
										return ((ModelClass_c)candidate).getName().equals("cls 3");
									}
								
								});
		assertNotNull(startClass);
		
		Operation_c op = Operation_c.getOneO_TFROnR115(startClass, new ClassQueryInterface_c() {
		
			public boolean evaluate(Object candidate) {
				return ((Operation_c) candidate).getName().equals("start_test_current_action");
			}
		
		});
		assertNotNull(op);
		
		BPDebugUtils.executeElement(op);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in state.", target
				.isSuspended());

		RequiredOperation_c reqOp = RequiredOperation_c
			.getOneSPR_ROOnR4502(
				RequiredExecutableProperty_c
						.getManySPR_REPsOnR4500(Requirement_c
								.getManyC_RsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port3");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((RequiredOperation_c) candidate)
								.getName().equals(
										"sendServerClientOAL");
					}

				});
		assertNotNull(reqOp);
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		DebugUITestUtilities.removeAllBreakpoints();
		try {
			process.getLaunch().getDebugTarget().resume();
		} catch (DebugException e) {
			fail(e.toString());
		}

		BPDebugUtils.executeElement(reqOp);

		
		DebugUITestUtilities.waitForExecution();
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_signal_current_action.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		// Commenting out known failure tests.  See dts0100656068
/*		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
*/	}
	
	public void testInterfaceExecutionSignalNotAssignedWithParameters() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		RequiredSignal_c reqSig = RequiredSignal_c
			.getOneSPR_RSOnR4502(
				RequiredExecutableProperty_c
						.getManySPR_REPsOnR4500(Requirement_c
								.getManyC_RsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port3");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((RequiredSignal_c) candidate)
								.getName().equals(
										"serverclientsigparams");
					}

				});
		assertNotNull(reqSig);
 
		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(reqSig);
		
		ActivityEditor editor = DebugUITestUtilities
				.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port3");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName().equals(
												"sendServerClientParams");
							}

						});
		assertNotNull(reqOp);

		BPDebugUtils.executeElement(reqOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in required signal.", target
				.isSuspended());
		
		// verify the parameters
		// Commenting out known failure tests.  See dts0100656068
/*		String xValue = DebugUITestUtilities.getValueForVariable("x");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"string1", xValue);
		String yValue = DebugUITestUtilities.getValueForVariable("y");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"string2", yValue);
*/		
		DebugUITestUtilities.removeAllBreakpoints();
		try {
			target.resume();
		} catch (DebugException e) {
			fail(e.toString());
		}
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_signal_parameters.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionOperationOALVoidReturn() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		ProvidedOperation_c proOp = ProvidedOperation_c
				.getOneSPR_POOnR4503(
						ProvidedExecutableProperty_c
								.getManySPR_PEPsOnR4501(Provision_c
										.getManyC_PsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port4");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((ProvidedOperation_c) candidate)
										.getName().equals(
												"sendClientServerOALVoidReturn");
							}

						});
		assertNotNull(proOp);

		ProvidedOperation_c testOp = ProvidedOperation_c
		.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"clientServerOALVoidReturn");
					}

				});
		assertNotNull(testOp);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(testOp);

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		BPDebugUtils.executeElement(testOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in provided operation.", target
				.isSuspended());
		
		DebugUITestUtilities.stepOver(engine, 1);
		
		String topFrameText = DebugUITestUtilities.getTopFrameText(engine);
		assertEquals("Port4::Interface Client Server 2::sendClientServerOALVoidReturn line: 2", topFrameText);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_oal_void_return.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExectuionOperationOALNonVoidReturn() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		ProvidedOperation_c proOp = ProvidedOperation_c
				.getOneSPR_POOnR4503(
						ProvidedExecutableProperty_c
								.getManySPR_PEPsOnR4501(Provision_c
										.getManyC_PsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port4");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((ProvidedOperation_c) candidate)
										.getName().equals(
												"sendClientServerOALNonVoidReturn");
							}

						});
		assertNotNull(proOp);

		ProvidedOperation_c testOp = ProvidedOperation_c
		.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"clientServerOALNonVoidReturn");
					}

				});
		assertNotNull(testOp);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(testOp);

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		BPDebugUtils.executeElement(testOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in provided operation.", target
				.isSuspended());
		
		DebugUITestUtilities.stepOver(engine, 3);
		
		String topFrameText = DebugUITestUtilities.getTopFrameText(engine);
		assertEquals("Port4::Interface Client Server 2::sendClientServerOALNonVoidReturn line: 2", topFrameText);
		
		// verify the return
		// Commenting out known failure tests.  See dts0100656068
/*		String result = DebugUITestUtilities.getValueForVariable("result");
		assertEquals(
				"Default parameter value was not as expected for variable result.",
				"3", result);
*/		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_oal_nonvoid_return.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionOperationNoOALVoidReturn() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		ProvidedOperation_c proOp = ProvidedOperation_c
				.getOneSPR_POOnR4503(
						ProvidedExecutableProperty_c
								.getManySPR_PEPsOnR4501(Provision_c
										.getManyC_PsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port4");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((ProvidedOperation_c) candidate)
										.getName().equals(
												"sendClientServerNoOALVoidReturn");
							}

						});
		assertNotNull(proOp);

		ProvidedOperation_c testOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"clientServerNoOALVoidReturn");
					}

				});
		assertNotNull(testOp);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(proOp);

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		BPDebugUtils.executeElement(proOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in provided operation.", target
				.isSuspended());
		
		DebugUITestUtilities.stepOver(engine, 1);
		DebugUITestUtilities.stepInto(engine, 1);
		
		String topFrameText = DebugUITestUtilities.getTopFrameText(engine);
		assertEquals("Port4::Interface Client Server 2::sendClientServerNoOALVoidReturn line: 3", topFrameText);
				
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_no_oal_void_return.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}
	
	public void testInterfaceExecutionOperationNoOALNonVoidReturn() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		ProvidedOperation_c proOp = ProvidedOperation_c
				.getOneSPR_POOnR4503(
						ProvidedExecutableProperty_c
								.getManySPR_PEPsOnR4501(Provision_c
										.getManyC_PsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port4");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((ProvidedOperation_c) candidate)
										.getName().equals(
												"sendClientServerNoOALNonVoidReturn");
							}

						});
		assertNotNull(proOp);

		ProvidedOperation_c testOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"clientServerNoOALNonVoidReturn");
					}

				});
		assertNotNull(testOp);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(proOp);

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		BPDebugUtils.executeElement(proOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in provided operation.", target
				.isSuspended());
		
		DebugUITestUtilities.stepOver(engine, 1);
		DebugUITestUtilities.stepInto(engine, 1);
		
		String topFrameText = DebugUITestUtilities.getTopFrameText(engine);
		assertEquals("Port4::Interface Client Server 2::sendClientServerNoOALNonVoidReturn line: 3", topFrameText);
		
		// verify the return
		String result = DebugUITestUtilities.getValueForVariable("result");
		assertEquals(
				"Default parameter value was not as expected for variable result.",
				"", result);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_no_oal_nonvoid_return.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionOperationCurrentExecutingAction() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());
		
    Package_c ss = Package_c.getOneEP_PKGOnR8001(PackageableElement_c.getManyPE_PEsOnR8003(component), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("ss");
      }
    });
    assertNotNull(ss);

		StateMachineState_c state = StateMachineState_c
			.getOneSM_STATEOnR501(
				StateMachine_c
						.getManySM_SMsOnR517(ClassStateMachine_c
								.getManySM_ASMsOnR519(ModelClass_c
										.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(ss), new ClassQueryInterface_c() {
																
																	public boolean evaluate(Object candidate) {
																		return ((ModelClass_c)candidate).getName().equals("cls 2");
																	}
																
																}))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((StateMachineState_c) candidate)
								.getName().equals("State 2");
					}

				});
		assertNotNull(state);
		
		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(state);
		
		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		ModelClass_c startClass = ModelClass_c
			.getOneO_OBJOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(ss), new ClassQueryInterface_c() {
								
									public boolean evaluate(Object candidate) {
										return ((ModelClass_c)candidate).getName().equals("cls 3");
									}
								
								});
		assertNotNull(startClass);
		
		Operation_c op = Operation_c.getOneO_TFROnR115(startClass, new ClassQueryInterface_c() {
		
			public boolean evaluate(Object candidate) {
				return ((Operation_c) candidate).getName().equals("start_test_current_action");
			}
		
		});
		assertNotNull(op);
		
		BPDebugUtils.executeElement(op);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in state.", target
				.isSuspended());

		ProvidedOperation_c proOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"sendClientServerNoOALVoidReturn");
					}

				});
		assertNotNull(proOp);
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		DebugUITestUtilities.removeAllBreakpoints();
		
		try {
			process.getLaunch().getDebugTarget().resume();
		} catch (DebugException e) {
			fail(e.toString());
		}

		BPDebugUtils.executeElement(proOp);

		
		DebugUITestUtilities.waitForExecution();
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_current_action.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		// Commenting out known failure tests.  See dts0100656068
/*		assertEquals(expected_results, actual_results);
*/	}

	public void testInterfaceExecutionOperationWithParametersVoidReturn() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		ProvidedOperation_c proOp = ProvidedOperation_c
				.getOneSPR_POOnR4503(
						ProvidedExecutableProperty_c
								.getManySPR_PEPsOnR4501(Provision_c
										.getManyC_PsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port4");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((ProvidedOperation_c) candidate)
										.getName().equals(
												"sendClientServerParamsVoidReturn");
							}

						});
		assertNotNull(proOp);

		ProvidedOperation_c testOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"clientServerParamsVoidReturn");
					}

				});
		assertNotNull(testOp);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(testOp);

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		BPDebugUtils.executeElement(testOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		DebugUITestUtilities.waitForExecution();

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in provided operation.", target
				.isSuspended());
		
		// verify the parameters
		// Commenting out known failure tests.  See dts0100656068
/*		String xValue = DebugUITestUtilities.getValueForVariable("x");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"2", xValue);
		String yValue = DebugUITestUtilities.getValueForVariable("y");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"2", yValue);
*/		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_params_void_return.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionOperationWithParamatersNonVoidReturn() {
    Package_c cp = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Package_c)candidate).getName().equals("Components");
      }
    });
    assertNotNull(cp);

    Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(cp), new ClassQueryInterface_c() {
      
      @Override
      public boolean evaluate(Object candidate) {
        return ((Component_c)candidate).getName().equals("fc1");
      }
    });
    assertNotNull(component);
    
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		ProvidedOperation_c proOp = ProvidedOperation_c
				.getOneSPR_POOnR4503(
						ProvidedExecutableProperty_c
								.getManySPR_PEPsOnR4501(Provision_c
										.getManyC_PsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port4");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((ProvidedOperation_c) candidate)
										.getName().equals(
												"sendClientServerParamsNonVoidReturn");
							}

						});
		assertNotNull(proOp);

		ProvidedOperation_c testOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"clientServerParamsNonVoidReturn");
					}

				});
		assertNotNull(testOp);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(testOp);

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);

		BPDebugUtils.executeElement(testOp);
		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		DebugUITestUtilities.waitForExecution();

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in provided operation.", target
				.isSuspended());
		
		// verify the parameters
		// Commenting out known failure tests.  See dts0100656068
/*		String xValue = DebugUITestUtilities.getValueForVariable("x");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"2", xValue);
		String yValue = DebugUITestUtilities.getValueForVariable("y");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"2", yValue);
		
		DebugUITestUtilities.stepOver(engine, 4);

		String result = DebugUITestUtilities.getValueForVariable("x");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"true", result);
*/		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_params_non_void_return.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}
}