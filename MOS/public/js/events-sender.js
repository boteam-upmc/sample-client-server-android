/**
 * @title Robot distance control
 * @author L.Adel
 * @date 21/12/2017
 */

/**
 * The angular velocity bar has been changed, update the value in the label
 * @param progress : range between 0 and 100
 */
function angVel (socket, progress) {
    console.log('[angVel] progress = ' + progress);
    socket.emit('set_angVel', progress);
}

/**
 * The time bar has been changed, set the mode to USE TIME and update the value in the label
 * @param progress : range between 0 and 100
 */
function time (socket, progress) {
    console.log('[time] progress = ' + progress);
    socket.emit('set_time', progress);
}

/**
 * The angle bar has been changed, set the mode to USE ANGLE and update the value in the label
 * @param progress : range between 0 and 100
 */
function angle (socket, progress) {
    console.log('[angle] progress = ' + progress);
    socket.emit('set_angle', progress);
}

/**
 * Control mode
 * @param isChecked : true or false
 */
function rcMode (socket, isChecked) {
    console.log('[rcMode] isChecked = ' + isChecked);
    socket.emit('set_rcMode', isChecked);
}

/**
 * Secure & Unsecure movement mode
 * @param isChecked : true or false
 */
function mode (socket, isChecked) {
    console.log('[mode] isChecked = ' + isChecked);
    socket.emit('set_mode', isChecked);
}

/**
 * IR led toggle mode
 * @param isChecked : true or false
 */
function leds (socket, isChecked) {
    console.log('[leds] isChecked = ' + isChecked);
    socket.emit('set_leds', isChecked);
}

/**
 * Pan & Tilt reset
 */
function reset (socket) {
    console.log('[reset] ' + null);
    socket.emit('set_reset', null);
}

/**
 * Move forward
 */
function forward (socket) {
    console.log('[forward] ' + null);
    socket.emit('set_forward', null);
}

/**
 * Turn left
 */
function left (socket) {
    console.log('[left] ' + null);
    socket.emit('set_left', null);
}

/**
 * Stop all movement
 */
function stop (socket) {
    console.log('[stop] ' + null);
    socket.emit('set_stop', null);
}

/**
 * Turn right
 */
function right (socket) {
    console.log('[right] ' + null);
    socket.emit('set_right', null);
}

/**
 * Move backward
 */
function backward (socket) {
    console.log('[backward] ' + null);
    socket.emit('set_backward', null);
}

/**
 * Pan management
 * @param progress : range between 0 and 100
 */
function pan (socket, progress) {
    console.log('[pan] progress = ' + progress);
    socket.emit('set_pan', progress);
}

/**
 * Tilt management
 * @param progress : range between 0 and 100
 */
function tilt (socket, progress) {
    console.log('[tilt] progress = ' + progress);
    socket.emit('set_tilt', progress);
}

/**
 * Status period
 * @param progress : range between 0 and 100
 */
function period (socket, progress) {
    console.log('[period] progress = ' + progress);
    socket.emit('set_period', progress);
}
