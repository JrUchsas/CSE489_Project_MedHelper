const express = require('express');
const router = express.Router();
const auth = require('../middleware/auth.middleware');
const { addMedication, getMedications, updateMedication, deleteMedication } = require('../controllers/medication.controller');

// @route   POST api/medications
// @desc    Add a new medication
// @access  Private
router.post('/', auth, addMedication);

// @route   GET api/medications
// @desc    Get all medications for a user
// @access  Private
router.get('/', auth, getMedications);

// @route   PUT api/medications/:id
// @desc    Update a medication
// @access  Private
router.put('/:id', auth, updateMedication);

// @route   DELETE api/medications/:id
// @desc    Delete a medication
// @access  Private
router.delete('/:id', auth, deleteMedication);

module.exports = router;