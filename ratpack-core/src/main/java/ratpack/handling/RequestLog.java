/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.internal.DefaultRequestLog;

/**
 * An object that formats information about a {@link RequestOutcome}.
 * By default the server provides the {@link DefaultRequestLog} which outputs the NCSA Common log format.
 *
 * The user can override the output format by adding an instance of this interface to the server registry.
 *
 * @see <a href="http://publib.boulder.ibm.com/tividd/td/ITWSA/ITWSA_info45/en_US/HTML/guide/c-logs.html#common">http://publib.boulder.ibm.com/tividd/td/ITWSA/ITWSA_info45/en_US/HTML/guide/c-logs.html#common</a>
 */
public interface RequestLog {

  /**
   * Format the provided {@link RequestOutcome} into a {@link String} suitable for logging.
   *
   * @param context the handler context
   * @param outcome the resulting outcome of a received request.
   * @return the formatted output to log.
   */
  String format(Context context, RequestOutcome outcome);

  /**
   * Adds a handler that logs each request.
   * The format of the log is defined an instance of {@link RequestLog} in the registry.
   * By default, the server provides an instance of {@link DefaultRequestLog} which outputs the NCSA Common log format.
   *
   * @return a handler that logs each request
   *
   */
  static Handler log() {
    return new Handler() {
      private final Logger logger = LoggerFactory.getLogger(RequestLog.class);

      @Override
      public void handle(Context ctx) throws Exception {
        ctx.onClose((RequestOutcome outcome) -> logger.info(ctx.get(RequestLog.class).format(ctx, outcome)));
        ctx.next();
      }
    };
  }
}
