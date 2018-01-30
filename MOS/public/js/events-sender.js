/**
 * @title Robot distance control. Send events from WEB to Node.js server.
 * @author L.Adel
 * @date 30/01/2018
 */

/**
 * The angular velocity bar has been changed, update the value in the label
 * @param progress : range between 0 and 350
 */
function onAngVel (socket, data) {
    console.log('[angVel] progress = ' + data.PROGRESS);
    socket.emit('set_angVel', data);
}

/**
 * The time bar has been changed, set the mode to USE TIME and update the value in the label
 * @param progress : range between 0 and 10000
 */
function onTime (socket, data) {
    console.log('[time] progress = ' + data.PROGRESS);
    socket.emit('set_time', data);
}

/**
 * The angle bar has been changed, set the mode to USE ANGLE and update the value in the label
 * @param progress : range between 0 and 720
 */
function onAngle (socket, data) {
    console.log('[angle] progress = ' + data.PROGRESS);
    socket.emit('set_angle', data);
}

/**
 * Control mode
 * @param isChecked : true or false
 */
function onRcMode (socket, data) {
    console.log('[rcMode] isChecked = ' + data.IS_CHECKED);
    socket.emit('set_rcMode', data);
}

/**
 * Secure & Unsecure movement mode
 * @param isChecked : true or false
 */
function onMode (socket, data) {
    console.log('[mode] isChecked = ' + data.IS_CHECKED);
    socket.emit('set_mode', data);
}

/**
 * IR led toggle mode
 * @param isChecked : true or false
 */
function onLeds (socket, data) {
    console.log('[leds] isChecked = ' + data.IS_CHECKED);
    socket.emit('set_leds', data);
}

/**
 * Pan & Tilt reset
 */
function onReset (socket, data) {
    console.log('[reset] ' + JSON.stringify(data));
    socket.emit('set_reset', data);
}

/**
 * Move forward
 */
function onForward (socket, data) {
    console.log('[forward] ' + JSON.stringify(data));
    socket.emit('set_forward', data);
}

/**
 * Turn left
 */
function onLeft (socket, data) {
    console.log('[left] ' + JSON.stringify(data));
    socket.emit('set_left', data);
}

/**
 * Stop all movement
 */
function onStop (socket, data) {
    console.log('[stop] ' + JSON.stringify(data));
    socket.emit('set_stop', data);
}

/**
 * Turn right
 */
function onRight (socket, data) {
    console.log('[right] ' + JSON.stringify(data));
    socket.emit('set_right', data);
}

/**
 * Move backward
 */
function onBackward (socket, data) {
    console.log('[backward] ' + JSON.stringify(data));
    socket.emit('set_backward', data);
}

/**
 * Pan management
 * @param progress : range between 0 and 340 (default progress value == 25)
 */
function onPan (socket, data) {
    console.log('[pan] progress = ' + data.PROGRESS);
    socket.emit('set_pan', data);
}

/**
 * Tilt management
 * @param progress : range between 0 and 110 (default progress value == 25)
 */
function onTilt (socket, data) {
    console.log('[tilt] progress = ' + data.PROGRESS);
    socket.emit('set_tilt', data);
}

/**
 * Status period
 * @param progress : range between 0 and 5000
 */
function onPeriod (socket, data) {
    console.log('[period] progress = ' + data.PROGRESS);
    socket.emit('set_period', data);
}
