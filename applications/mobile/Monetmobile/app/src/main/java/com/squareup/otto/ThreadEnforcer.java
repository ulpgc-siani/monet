/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.squareup.otto;

import android.os.Looper;

/**
 * Enforce the actions for a particular event bus are occurring on a valid
 * thread.
 * 
 * @author Jake Wharton
 */
public interface ThreadEnforcer {

  /**
   * Enforce a valid thread for the given {@code bus}.
   * 
   * @param bus
   *          Event bus instance on which an action is being performed.
   */
  void enforce(Bus bus);

  /** A {@link ThreadEnforcer} which does no verification. */
  ThreadEnforcer ANY  = new ThreadEnforcer() {
                        @Override
                        public void enforce(Bus bus) {
                          // Allow any thread.
                        }
                      };

  /**
   * A {@link ThreadEnforcer} which ensures you are operating on the main
   * thread.
   */
  ThreadEnforcer MAIN = new ThreadEnforcer() {
                        @Override
                        public void enforce(Bus bus) {
                          if (Looper.myLooper() != Looper.getMainLooper()) {
                            throw new IllegalStateException("Event bus " + bus + " accessed from non-main thread " + Looper.myLooper());
                          }
                        }
                      };

}
