const express = require('express');
const router = express.Router();
const auth = require('../middleware/auth.middleware');
const { addFamilyMember, getFamilyMembers, deleteFamilyMember } = require('../controllers/family.controller');

// @route   POST api/family
// @desc    Add a new family member
// @access  Private
router.post('/', auth, addFamilyMember);

// @route   GET api/family
// @desc    Get all family members for a user
// @access  Private
router.get('/', auth, getFamilyMembers);

// @route   DELETE api/family/:id
// @desc    Delete a family member
// @access  Private
router.delete('/:id', auth, deleteFamilyMember);

module.exports = router;