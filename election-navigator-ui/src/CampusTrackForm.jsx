import React, { useState } from 'react';

const CampusTrackForm = () => {
  const [file, setFile] = useState(null);
  const [status, setStatus] = useState('FOUND'); // Default to found
  const [location, setLocation] = useState('');
  const [isUploading, setIsUploading] = useState(false);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file || !location) {
      alert("Please upload an image and enter a location.");
      return;
    }

    setIsUploading(true);

    // Package the image and data to send to Java
    const formData = new FormData();
    formData.append('image', file);
    formData.append('status', status);
    formData.append('location', location);

    try {
      // We will point this to your Java backend endpoint
      const response = await fetch('http://localhost:8080/api/campustrack/report', {
        method: 'POST',
        body: formData, // Notice we do NOT use JSON.stringify for files!
      });

      if (response.ok) {
        alert("Item reported successfully! The AI is tagging it now.");
        setFile(null);
        setLocation('');
      } else {
        alert("Something went wrong.");
      }
    } catch (error) {
      console.error("Upload failed:", error);
    } finally {
      setIsUploading(false);
    }
  };

  return (
    <div className="campus-track-card" style={{ padding: '20px', border: '1px solid #ccc', borderRadius: '8px', maxWidth: '400px', margin: '20px auto' }}>
      <h3>Report an Item 📸</h3>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
        
        <select value={status} onChange={(e) => setStatus(e.target.value)} style={{ padding: '10px' }}>
          <option value="FOUND">I Found Something</option>
          <option value="LOST">I Lost Something</option>
        </select>

        <input 
          type="text" 
          placeholder="Where was it? (e.g., Canteen, Block A)" 
          value={location} 
          onChange={(e) => setLocation(e.target.value)} 
          style={{ padding: '10px' }}
        />

        <input 
          type="file" 
          accept="image/*" 
          onChange={handleFileChange} 
          style={{ padding: '10px' }}
        />

        <button type="submit" disabled={isUploading} style={{ padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
          {isUploading ? "Uploading & Analyzing..." : "Submit Report"}
        </button>
      </form>
    </div>
  );
};

export default CampusTrackForm;