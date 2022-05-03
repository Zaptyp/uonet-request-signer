const signer = require("@wulkanowy/uonet-request-signer/signer");

forge = require('node-forge');
crypto = require('crypto');

function signContent(password, certificate, content) {
    return signer.signContent(password, certificate, content);
}

module.exports = { signContent };
