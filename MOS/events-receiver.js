/**
 * @title Robot distance control
 * @author L.Adel
 * @date 21/12/2017
 */

var socket;

/**
 * Handle robot distance control events
 */
module.exports = function (mySocket) {
    socket = mySocket;
    
    angVel();
    time();
    angle();
    rcMode();
    mode();
    leds();
    reset();
    forward();
    left();
    stop();
    right();
    backward();
    pan();
    period();
}

/**
 * The angular velocity bar has been changed, update the value in the label
 */
function angVel () {
    socket.on('set_angVel', function (progress) {
        console.log('[angVel] progress = ' + progress);
        socket.emit('get_angVel', progress);
    });
}

/**
 * The time bar has been changed, set the mode to USE TIME and update the value in the label
 */
function time () {    
    socket.on('set_time', function (progress) {
        console.log('[time] progress = ' + progress);
        socket.emit('get_time', progress);
    });
}

/**
 * the angle bar has been changed, set the mode to USE ANGLE and update the value in the label
 */
function angle () {    
    socket.on('set_angle', function (progress) {
        console.log('[angle] progress = ' + progress);
        socket.emit('get_angle', progress);
    });
}

/**
 * Contro mode
 */
function rcMode () {    
    socket.on('set_rcMode', function (isChecked) {
        console.log('[rcMode] isChecked = ' + isChecked);    
        socket.emit('get_rcMode', isChecked);
    });
}

/**
 * Secure & Unsecure movement mode
 */
function mode () {    
    socket.on('set_mode', function (isChecked) {
        console.log('[mode] isChecked = ' + isChecked);    
        socket.emit('get_mode', isChecked);
    });
}

/**
 * IR led toggle mode
 */
function leds () {        
    socket.on('set_leds', function (isChecked) {
        console.log('[leds] isChecked = ' + isChecked);
        socket.emit('get_leds', isChecked);
    });
}

/**
 * Pan & Tilt reset
 */
function reset () {        
    socket.on('set_reset', function () {
        console.log('[reset] ' + null);
        socket.emit('get_reset', null);
    });
}

/**
 * Move forward
 */
function forward () {
    socket.on('set_forward', function () {
        console.log('[forward] ' + null);
        socket.emit('get_forward', null);
    });
}

/**
 * Turn left
 */
function left () {
    socket.on('set_left', function () {
        console.log('[left] ' + null);
        socket.emit('get_left', null);
    });
}

/**
 * Stop all movement
 */
function stop () {
    socket.on('set_stop', function () {
        console.log('[stop] ' + null);
        socket.emit('get_stop', null);
    });
}

/**
 * Turn right
 */
function right () {
    socket.on('set_right', function () {
        console.log('[right] ' + null);
        socket.emit('get_right', null);
    });
}

/**
 * Move backward
 */
function backward () {
    socket.on('set_backward', function () {
        console.log('[backward] ' + null);
        socket.emit('get_backward', null);
    });
}

/**
 * Pan management
 */
function pan () {
    socket.on('set_pan', function (progress) {
        console.log('[pan] progress = ' + progress);
        socket.emit('get_pan', progress);
    });
}

/**
 * Status period
 */
function period () {
    socket.on('set_period', function (progress) {
        console.log('[period] progress = ' + progress);
        socket.emit('get_period', progress);
    });
}
