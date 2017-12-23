/**
 * @title Robot distance control
 * @author L.Adel
 * @date 21/12/2017
 */

/**
 * The angular velocity bar has been changed, update the value in the label
 * @param progress : range between 0 and 350
 */
function onAngVel (socket, progress) {
    console.log('[angVel] progress = ' + progress);
    socket.emit('set_angVel', progress);
}

/**
 * The time bar has been changed, set the mode to USE TIME and update the value in the label
 * @param progress : range between 0 and 10000
 */
function onTime (socket, progress) {
    console.log('[time] progress = ' + progress);
    socket.emit('set_time', progress);
}

/**
 * The angle bar has been changed, set the mode to USE ANGLE and update the value in the label
 * @param progress : range between 0 and 720
 */
function onAngle (socket, progress) {
    console.log('[angle] progress = ' + progress);
    socket.emit('set_angle', progress);
}

/**
 * Control mode
 * @param isChecked : true or false
 */
function onRcMode (socket, isChecked) {
    console.log('[rcMode] isChecked = ' + isChecked);
    socket.emit('set_rcMode', isChecked);
}

/**
 * Secure & Unsecure movement mode
 * @param isChecked : true or false
 */
function onMode (socket, isChecked) {
    console.log('[mode] isChecked = ' + isChecked);
    socket.emit('set_mode', isChecked);
}

/**
 * IR led toggle mode
 * @param isChecked : true or false
 */
function onLeds (socket, isChecked) {
    console.log('[leds] isChecked = ' + isChecked);
    socket.emit('set_leds', isChecked);
}

/**
 * Pan & Tilt reset
 */
function onReset (socket) {
    console.log('[reset] ' + null);
    socket.emit('set_reset', null);
}

/**
 * Move forward
 */
function onForward (socket) {
    console.log('[forward] ' + null);
    socket.emit('set_forward', null);
}

/**
 * Turn left
 */
function onLeft (socket) {
    console.log('[left] ' + null);
    socket.emit('set_left', null);
}

/**
 * Stop all movement
 */
function onStop (socket) {
    console.log('[stop] ' + null);
    socket.emit('set_stop', null);
}

/**
 * Turn right
 */
function onRight (socket) {
    console.log('[right] ' + null);
    socket.emit('set_right', null);
}

/**
 * Move backward
 */
function onBackward (socket) {
    console.log('[backward] ' + null);
    socket.emit('set_backward', null);
}

/**
 * Pan management
 * @param progress : range between 0 and 340 (default progress value == 25)
 */
function onPan (socket, progress) {
    console.log('[pan] progress = ' + progress);
    socket.emit('set_pan', progress);
}

/**
 * Tilt management
 * @param progress : range between 0 and 110 (default progress value == 25)
 */
function onTilt (socket, progress) {
    console.log('[tilt] progress = ' + progress);
    socket.emit('set_tilt', progress);
}

/**
 * Status period
 * @param progress : range between 0 and 5000
 */
function onPeriod (socket, progress) {
    console.log('[period] progress = ' + progress);
    socket.emit('set_period', progress);
}
